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

This directory contains the query benchmarking input (numbered directories) and experimental results (data.txt), as well as the input database (sanitized.txt). The original paper is also included here. The directory structure for the benchmarking files is: prefix_length/number_of_queries/number_of_results[a-c]. There are three files for each case to make up for the large number of prefixes which will return zero results. For example, the prefixes "xxz" or "trn" will not match any strings in the database, resultsing in skewed benchmarking values.

TrieLAIS/
=========

<strong>src/</strong>

This directory contains the modified Trie implementation, which solves the Location-aware Instant Search problem. It is also currently in a state that runs the benchmarks, similar to main() for the PRTree. 

There is a GUI interface in GUI.java that returns results as they are typed.

<strong>data/</strong>

This directory is the same as the PRTree case.

ruby_files/
===========

The ruby files that were used to gather and modify input data are contained here. The data.rb file is meant to work with a Factual API key. The generateData.rb file is used to create the sample input queries. The removeDuplicates.rb is used to remove duplicates from the database. The results are then run through sanitize.rb, which performs the sanitizing steps listed in the report.
