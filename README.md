PRTree
======

Prefix-Region Tree in Java

The DBProject and TrieLAIS directories were created in Eclipse. If you use Eclipse, I would imagine that importing the directories themselves will allow you to use the programs as you would with anything else.

Otherwise, the src/ directories contain straightforward data files, which can be used like any other plain Java project.

DBProject/
==========

<strong>src/</strong>

This directory contains the PR-Tree implementation, in PRTRee.java. The constructPRTree and kNNQuery algorithms are here. Currently, the main() function does not print any search results, but performs the benchmarking test instead.

<strong>data/</strong>

This directory contains the query benchmarking input (numbered directories) and experimental results (data.txt), as well as the input data (sanitized.txt). The naming convention for the benchmarking files is: number_of_queries.prefix_length.number_of_results
