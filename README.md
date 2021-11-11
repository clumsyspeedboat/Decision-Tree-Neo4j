# Decision Tree Plug-in (DTP) for Neo4j
A plug-in to create decision tree algorithms in Neo4j.

## About

Graph databases enable efficient storage of heterogeneous, highly-interlinked
data, such as clinical data. Subsequently, researchers can extract relevant features from
these datasets and apply machine learning for diagnosis, bio-marker discovery, or understanding
pathogenesis. To facilitate this process by saving time for extracting data
from the graph database, we developed DTP (Decision Tree Plug-in), containing 19 procedures
to assist and generate decision tree algorithms directly on the graph database
Neo4j. As a result, our approach required between 0.059 - 0.099 seconds for creating
a decision tree of three clinical test data sets, while the csv-based Java decision tree
needed 0.085 - 0.112 seconds. Furthermore, our approach proved to be the fastest across
baselines in R and Python using csv files instead of nodes as input. A bonus feature
being that DTP persists the decision tree in Neo4j allowing for direct visual analysis.
Overall, our work shows that integrating machine learning into graph databases saves
time and should be applied to a variety of use cases including clinical environments.

## Features

DTP comprises of 19 procedures, which read CSV-files, map nodes, split data,
generate decision tree using 3 different metrics , perform k-fold cross
validation, validate the classifier and visualize tree.

## Methodology

![DTP_Methodology](https://user-images.githubusercontent.com/19682074/141226264-6815a517-2221-42a6-b393-44725b89afbc.PNG)


## [Compile the JAR File Yourself](https://github.com/clumsyspeedboat/Decision-Tree-Neo4j/wiki/Install-Decision-Tree-Plugin-in-Neo4j)
## [Download the JAR File](https://github.com/clumsyspeedboat/Decision-Tree-Neo4j/tree/main/Jar%20File)

