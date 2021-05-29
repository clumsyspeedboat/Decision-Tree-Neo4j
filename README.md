# Graph-Database-Learning-Algorithms-Neo4j-
A project to survey the possibilities of a graph database Neo4j in building a machine learning algorithm on clinical data


## Work Load

6 * 30 * 6 = 1080 hours

## Members

* Minh Dung Do - minh.do@st.ovgu.de
* Nasim Uddin Ahmed - nasim.ahmed@st.ovgu.de
* Seles Selvan - seles.selvan@st.ovgu.de
* Taha Bin Imran - taha.imran@st.ovgu.de
* Yumna Iqbal - yumna.iqbal@st.ovgu.de
* Rahul Mondal - rahulmondal415@gmail.com

## Goal 
The goal of this project is to accelerated the analysis of highly interlinked clinical and biological data using graph databases. Therefore, we will try to integrate machine learning algorithms into the graph structure

* What are the time-consuming steps (data access, data organization, training of the network) of machine learning algorithms, and what are the interfaces to connect machine learning algorithms to data provisioning frameworks (e.g. graph databases)?
* How can we optimize the data provisioning of a graph database for machine learning algorithms (e.g. by special machine-learning-agnostic operators of the graph database or suitable indexing)?
* What are components of a graph database that can be adapted to efficiently integrate a machine learning algorithm and how big is their impact on the performance?


### Milestone 1 - KickOff presentation about the whole project and the team (40 hours)

```
* Kick off presentation 
* Member Roles discussed
* Data Files Collected for Testing & Training

```
### Milestone 2 - Survey of graph-databases and data organisation (40 hours), Detailled decription of neo4j focussing on data access for machine learning and GPU computing (80 hours)

```
* Survey of Graph Databases - Yumna, Seles
* Prospects of Neo4j - Yumna, Seles
* Neo4j focussing on Data Access - Seles
* GPU Computing - Danny

```
### Milestone 3 - Survey of implementations for decision tree learning and neuronal networks (40 hours), Implementation of decision tree learning algorithm in Java (80 hours), Evaluation of decision tree learning algorithm against the results of a standard library  (40 hours) 

```
* Deadline for Milestone 3: 06/05/2021

Task in Milestone 3:
* Finding suitable dataset for Decision Tree Algorithm - Rahul
* Survey of Decision Tree algorithms in Java - Nasim, Danny
* Implementation of Decision Tree in WEKA - Seles
* Prospects of Decision Tree algorithms being implemented on a graph database like Neo4j - Nasim, Danny

```

### Milestone 4 - Apply decision tree learning algorithm directly on graph and compare the performance

```
* Deadline for Milestone 4: 20/05/2021

Possibility 1 procedures: Java Plugin in Neo4j
- Setup plugin project in eclipse using Maven.
- Add the complile jar file into Neo4j project plugin directory.
- Using Cypher to send the csv file path to the Java plugin.
- The Java plugin then return the necessary node and relationship that need to create the Decision Tree.
- Neo4j will read the return of the Java plugin and visualise the Decision Tree with node and relationship.


Possibility 2 procedures: Pure Neo4j cypher
- Using Cypher to input the dataset (csv file) into Neo4j to create the Graph Database.
- Traverse through the Graph Database to calculate the information gain/Gini-entropy.
- Create the Decision Tree with node and relationship.
```

### Milestone 5 - Sum up results in a 8-10 page paper (100 hours)

```
* Latex in Overleaf & Citation using JabRef - Taha
* Summing up project report for milestone 1 - Rahul

```

### Milestone 6 - Final Presentation (20 hours)
