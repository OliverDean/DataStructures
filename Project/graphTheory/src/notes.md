

Question 1. Write a method that, given a pair of pages, returns the minimum number of links you must follow to get from the first page to the second.

the graph is directed but unweighted. the shortest path from one vertex to another is the minimum number of edges to be traversed. done so by using a breadth first search. must use an adjacency list not a matrix. do not use Dijkstra's algorithm in q1 its only faster in a weighted graph. 
wiki psudocode for bfs

 1  procedure BFS(G, root) is
 2      let Q be a queue
 3      label root as explored
 4      Q.enqueue(root)
 5      while Q is not empty do
 6          v := Q.dequeue()
 7          if v is the goal then
 8              return v
 9          for all edges from v to w in G.adjacentEdges(v) do
10              if w is not labeled as explored then
11                  label w as explored
12                  w.parent := v
13                  Q.enqueue(w)



Question 2. Write a method that finds a Hamiltonian path in a Wikipedia page graph. A Hamiltonian path is any path in some graph that visits every vertex exactly once. This method will never be called for graphs with more than 20 pages.

best known solution is the bellman-held-Karp algorithm which uses dynamic programming.  "Is there a path ending at vertex  that visits every vertex in the subset iP exactly once?".
This algorithm has a worst-case time complexity of , which is the best known.O(V^2*2^V) 


wiki psudocode
https://en.wikipedia.org/wiki/Held%E2%80%93Karp_algorithm#cite_note-4

function algorithm TSP (G, n) is
    for k := 2 to n do
        g({k}, k) := d(1, k)
    end for

    for s := 2 to n−1 do
        for all S ⊆ {2, ..., n}, |S| = s do
            for all k ∈ S do
                g(S, k) := minm≠k,m∈S [g(S\{k}, m) + d(m, k)]
            end for
        end for
    end for

    opt := mink≠1 [g({2, 3, ..., n}, k) + d(k, 1)]
    return (opt)
end function


Question 3. Write a method that finds every ‘strongly connected component’ of pages. A strongly connected component is a set of vertices such that there is a path between every ordered pair of vertices in the strongly connected component.

utalizing Kosaraju's algorithm to compute the scc. 

Questions 4. Write a method that finds all the centers of the Wikipedia page graph. A vertex is considered to be the center of a graph if the maximum shortest path from that vertex to any other vertex is the minimum possible.

