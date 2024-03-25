#include <stdio.h>
#include <stdlib.h>
#include <windows.h>

#define ARRAY_SIZE 100
#define NUM_THREADS 4

int array[ARRAY_SIZE];

typedef struct {
    int* start;
    size_t size;
} SortArgs;

int cmpfunc(const void* a, const void* b) {
    return (*(int*)a - *(int*)b);
}

DWORD WINAPI sort_chunk(LPVOID arg) {
    SortArgs* args = (SortArgs*)arg;
    qsort(args->start, args->size, sizeof(int), cmpfunc);
    return 0;
}

void merge(int* arr, size_t left, size_t middle, size_t right) {
    size_t left_size = middle - left + 1;
    size_t right_size = right - middle;
    int* left_arr = malloc(left_size * sizeof(int));
    int* right_arr = malloc(right_size * sizeof(int));
    if (left_arr == NULL || right_arr == NULL) {
        printf("Memory allocation failed.\n");
        exit(1);
    }
    for (size_t i = 0; i < left_size; i++)
        left_arr[i] = arr[left + i];
    for (size_t j = 0; j < right_size; j++)
        right_arr[j] = arr[middle + 1 + j];
    size_t i = 0, j = 0, k = left;
    while (i < left_size && j < right_size) {
        if (left_arr[i] <= right_arr[j]) {
            arr[k] = left_arr[i];
            i++;
        } else {
            arr[k] = right_arr[j];
            j++;
        }
        k++;
    }
    while (i < left_size) {
        arr[k] = left_arr[i];
        i++;
        k++;
    }
    while (j < right_size) {
        arr[k] = right_arr[j];
        j++;
        k++;
    }
    free(left_arr);
    free(right_arr);
}

DWORD WINAPI merge_chunks(LPVOID arg) {
    size_t chunk_size = ARRAY_SIZE / NUM_THREADS;
    size_t left = 0, middle, right = chunk_size - 1;
    for (size_t i = 0; i < NUM_THREADS - 1; i++) {
        middle = left + (right - left) / 2;
        merge(array, left, middle, right);
        left += chunk_size;
        right += chunk_size;
    }
    merge(array, left, left + (right - left) / 2, ARRAY_SIZE - 1);
    return 0;
}

int main() {
    HANDLE threads[NUM_THREADS];
    SortArgs args[NUM_THREADS];
    for (int i = 0; i < ARRAY_SIZE; i++)
        array[i] = rand() % 1000;
    for (int i = 0; i < NUM_THREADS; i++) {
        args[i].start = array + i * (ARRAY_SIZE / NUM_THREADS);
        args[i].size = ARRAY_SIZE / NUM_THREADS;
        threads[i] = CreateThread(NULL, 0, sort_chunk, (LPVOID)&args[i], 0, NULL);
        if (threads[i] == NULL) {
            printf("Error creating thread.\n");
            exit(1);
        }
    }
    WaitForMultipleObjects(NUM_THREADS, threads, TRUE, INFINITE);
    merge_chunks(NULL);
    printf("Sorted Array:\n");
    for (int i = 0; i < ARRAY_SIZE; i++)
        printf("%d ", array[i]);
    printf("\n");
    for (int i = 0; i < NUM_THREADS; i++)
        CloseHandle(threads[i]);
    return 0;
}
