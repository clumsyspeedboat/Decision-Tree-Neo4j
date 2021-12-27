# Decision Tree Plug-in (DTP) for Neo4j
A plug-in to create decision tree algorithms in Neo4j with the following splitting criteria
* **Gini Index**
* **Information Gain**
* **Gain Ratio**

## [About](https://github.com/clumsyspeedboat/Decision-Tree-Neo4j/wiki#About)
## Methodology

![DTP_Methodology](https://user-images.githubusercontent.com/19682074/141226264-6815a517-2221-42a6-b393-44725b89afbc.PNG)


### [Compile the JAR File of DTP Yourself](https://github.com/clumsyspeedboat/Decision-Tree-Neo4j/wiki/Install-Decision-Tree-Plugin-in-Neo4j)
### [Download the JAR File of DTP](https://github.com/clumsyspeedboat/Decision-Tree-Neo4j/tree/main/Jar%20File)

## Data
* [Dataset 1 : Heart Failure Prediction](https://github.com/clumsyspeedboat/Decision-Tree-Neo4j/blob/main/Dataset%201%20-%20Heart%20Failure%20Prediction/Heart%20Failure%20Prediction.csv)
* [Dataset 2 : Prediction of Inflammatory BowelDisease from Microbiome](https://github.com/clumsyspeedboat/Decision-Tree-Neo4j/blob/main/Dataset%202%20-%20Metaprotein/Metaprotein_50.csv)
* [Dataset 3 : H1N1 COVID19 Classification](https://github.com/clumsyspeedboat/Decision-Tree-Neo4j/blob/main/Dataset%203%20-%20Flu%20Classification/Flu_Classification.csv)

## Features

DTP comprises of 19 procedures, which read CSV-files, map nodes, split data,
generate decision tree using 3 different metrics , perform k-fold cross
validation, validate the classifier and visualize tree.

## Procdures to create Decision Tree
### [- from Nodes in Neo4j](https://github.com/clumsyspeedboat/Decision-Tree-Neo4j/wiki/Decision-Tree-from-Nodes)
### [- from CSV file paths](https://github.com/clumsyspeedboat/Decision-Tree-Neo4j/wiki/Decision-Tree-from-CSV-files)
### [- Cross validation (nodes or CSV files)](https://github.com/clumsyspeedboat/Decision-Tree-Neo4j/wiki/Cross-validation-(nodes-or-CSV-files))
### [- Confusion matrix (from CSV files without tree visualization)](https://github.com/clumsyspeedboat/Decision-Tree-Neo4j/wiki/Confusion-matrix-(from-CSV-files-without-tree-visualization))
