package com.nunez.finance.service;

import com.nunez.finance.domain.PrimeNumberService;
import lombok.extern.slf4j.Slf4j; // For logging
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PrimeNumberServiceImpl implements com.nunez.finance.domain.PrimeNumberService { // Implement the domain interface

    /**
     * Checks if a number is prime.
     * A prime number is a natural number greater than 1 that has no positive divisors other than 1 and itself.
     * @param n The number to check.
     * @return true if n is prime, false otherwise.
     */
    private boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }
        if (n <= 3) {
            return true;
        }
        if (n % 2 == 0 || n % 3 == 0) {
            return false;
        }
        // Check divisibility from 5 upwards, with a step of 6 (i.e., check i and i+2)
        // This optimization skips multiples of 2 and 3.
        for (int i = 5; i * i <= n; i = i + 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Integer> getFirstNPrimes(int n) {
        if (n <= 0) {
            log.warn("Requested number of primes is non-positive: {}", n);
            return new ArrayList<>(); // Return empty list for invalid input
        }

        List<Integer> primes = new ArrayList<>();
        int num = 2; // Start checking from the first prime number

        log.info("Generating the first {} prime numbers.", n);
        while (primes.size() < n) {
            if (isPrime(num)) {
                primes.add(num);
            }
            num++;
        }
        log.info("Successfully generated {} prime numbers.", primes.size());
        return primes;
    }
}
