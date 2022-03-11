#include <iostream>
#include <thread>

using namespace std;

int numb1 = 10; // Start number
int numb2 = 1000; // End number
int arr[0] = {};  // size of numb2
int arr2[0] = {}; // size equal to number of primes.

// Method for checking if a number is divisible by any numbers
int findPrime(int i) {
    for (int j = 2; j <= i / 2; j++) {
        if (i % j == 0) {
            return 0;
        }
    }
    return i;
}

int main() {
    int sum1 = 0; // Counts number of primes in thread 1.
    int sum2 = 0; // Counts number of primes in thread 2.
    mutex sum_mutex; // thread lock

    // Thread 1: Checks first half of the numbers for primes
    thread t1([&sum1] {
        for (int i = numb1; i < (numb1 + numb2) / 2; i++) {
            if (findPrime(i) != 0) {
                arr[i] = findPrime(i); // insert prime based on its value.
                sum1++;
            }
        }
    });

    // Thread 2: Checks last half of numbers for primes
    thread t2([&sum2] {
        for (int i = (numb1 + numb2) / 2; i < numb2; i++) {
            if (findPrime(i) != 0) {
                arr[i] = findPrime(i);
                sum2++;
            }
        }
    });

    // Join threads
    t1.join();
    t2.join();

    // Total number of primes
    int sum = sum1 + sum2;

    // Spot in array for next prime
    int next = 0;

    // Fills arr2 with all numbers larger than 0 (removes empty spots).
    for (int i = 0; i < numb2; i++) {
        if (arr[i] != 0) {
            arr2[next] = i;
            next++;
        }
    }

    // prints all prime numbers.
    for (int i = 0; i < sum; i++)
        cout << arr2[i] << "\n";
}
