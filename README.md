# ThreadSyncWait4AnEvent
Process synchronization for “Wait for an event” using java threads, lock, condition variable, and predicate variable.

In this exercise we want to ensure the occurrence of one event
(i-e displaying “ab” by parent process) before the occurrence of
another event (i-e displaying “cd” by the child process).

Program Design Description:

- Creates a mutex, a condition variable, and a predicate variable. These three will work together to synchronize occurrence of the required events. Initially the value of the predicate variable is set to true.

- Create thread1. The thread1 acquires the mutex and checks the value of the predicate variable. If the value is false then it waits on condition variable until it is signaled. When it is signaled, it calls the display(“ab”) function and then sets the value of the predicate to false. Then it signals the condition variable and releases the mutex.

- Create thread2. The thread2 acquires the mutex and checks the value of the predicate variable. If the value is true then it waits on condition variable until it is signaled. When it is signaled, it calls the display(“cd\n”) function and then sets the value of the predicate to true. Then it signals the condition variable and releases the mutex.

- “main” thread will wait for thread1 and thread2 to finish execution
