An application we are developing requires a caching mechanism. The cache’s purpose is to provide 
a fast and efficient way of retrieving strings. It needs to meet the following requirements:

Fixed Size: The cache needs to have some bounds to limit memory usages.
Fast Access: The cache’s insert and lookup operations should be fast , preferably O(1) time
Replacement of objects when the cache memory limit is reached: The cache should have an efficient algorithm 
to evict the least often used object when it’s memory is full.



