#include <iostream>
#include <vector>
#include <thread>
#include <list>
#include <functional>
#include <mutex>
#include <condition_variable>
#include <atomic>

using namespace std;

class Workers{
    vector<thread> worker_threads;
    list<function<void()>> tasks;
    int nr_of_threads;
    mutex task_mutex;
    condition_variable cv_lock;
    atomic<bool> run_loop{true};


public:
    Workers(int given_nr_of_threads){
        nr_of_threads = given_nr_of_threads;
    };

    void start(){
        run_loop = true;
        for (int i = 0; i < nr_of_threads; ++i) {
            worker_threads.emplace_back([&]{
                while (true){
                    function<void()> task;
                    {
                        unique_lock<mutex> lock(task_mutex);
                        while (tasks.empty()){
                            if (!run_loop) {
                                return;
                            }
                            cv_lock.wait(lock);
                        }
                        task = *tasks.begin();
                        tasks.pop_front();
                    }
                    if(task){
                        task();
                    }
                }
            });
        }
    }

    void post(function<void()> task){
        tasks.emplace_back(task);
        cv_lock.notify_one();
    }

    void join(){
        for (auto &thread : worker_threads){
            thread.join();
        }
    }

    void stop(){
        run_loop.exchange(false);
        cv_lock.notify_all();
    }


    void post_timeout(function<void()> task, int time)
    {
        tasks.emplace_back([time, task] {
            this_thread::sleep_for(chrono::milliseconds(time));
            task();
        });
    }
};


int main() {
    mutex c_mutex;
    condition_variable c_cv;
    Workers worker_threads(4);
    Workers event_loop(1);

    worker_threads.start();// Create 4 internal threads
    event_loop.start(); // Create 1 internal thread

    worker_threads.post([]{
        this_thread::sleep_for(2s);
        cout << "Task A\n";
    });


    worker_threads.post([] {
        this_thread::sleep_for(2s);
        cout << "Task B\n";
    });

    unique_lock<mutex> lock(c_mutex);
    event_loop.post([&c_cv] {
        this_thread::sleep_for(2s);
        cout << "Task C\n";
        c_cv.notify_one();
    });

    c_cv.wait(lock);
    event_loop.post([] {
        cout << "Task D\n";
    });

    worker_threads.post_timeout([]{
        cout << "Post Timeout" << endl;
    }, 2500);
    
    worker_threads.stop();
    event_loop.stop();

    worker_threads.join(); // Calls join() on the worker threads
    event_loop.join(); // Calls join() on the event thread
}