###################
## Meta protein ### ---> DATA ANALYSIS + DECISION TREE ALGORITHMS
###################

# Cleaning the work space #

cat("\f")       # Clear old outputs
rm(list=ls())   # Clear all variables

if(!require("factoextra")) install.packages("factoextra") # PCA
if(!require("FSelector")) install.packages("FSelector")   # Information Gain & Gain Ratio
if(!require("DescTools")) install.packages("DescTools")   # Gini Index
if(!require("rpart")) install.packages("rpart")           # Decision Tree : CART (gini)
if(!require("rpart.plot")) install.packages("rpart.plot") # Decision Tree plot : CART
if(!require("C50")) install.packages("C50")               # Decision Tree : C 5.0 (gain ratio)
if(!require("RWeka")) install.packages("RWeka")           # Decision Tree : C 4.5 (gain ratio)

library("factoextra")
library("FSelector")
library("DescTools")
library("rpart")
library("rpart.plot")
library("C50")
library("RWeka")

# Creating a data matrix #

data1 <- file.choose()
data1

data_matrix <- read.csv(data1, header = TRUE, sep = ",")


####################
## Pre Processing ##
####################

## Removing unnecessary rows ##

data_matrix <- data_matrix[1:2969,]


## To set metaprotein names as row names/identifier ##

# rownames(data_matrix) <- data_matrix[,1]


## Taking a subset of the data by choosing a certain Class or Species ##

# data_matrix_m <- data_matrix[data_matrix$Class == "Mammalia",]
# data_matrix_hs <- data_matrix[data_matrix$Species == "Homo sapiens",]


## Performing row sums : Do not perform this step if the decision tree codes below are to be implemented; ##
## otherwise make necessary changes ##

# Control_Patients <- as.numeric(rowSums(data_matrix[,c(13:33)]))
# CD_Patients <- as.numeric(rowSums(data_matrix[,c(34:46)]))
# UC_Patients <- as.numeric(rowSums(data_matrix[,c(37:60)]))
# data_matrix_patients <- as.data.frame(cbind(data_matrix, Control_Patients, CD_Patients, UC_Patients))
 

## Removing unnecessary columns and taking just the numeric columns of patient data ##

data_matrix <- data_matrix[,13:60]


## Transpose the matrix to set instances as rows and variables as columns ##

data_matrix_new <- t(data_matrix)
data_matrix_new <- as.data.frame(data_matrix_new)


##  Generating a class label - Control. CD & UC Patients labeled accordingly ##

Patient.Type <- matrix("C",)
data_matrix_new <- cbind(data_matrix_new, Patient.Type) 

for (i in 21:33) {
  
  data_matrix_new[i,2970] <- "CD"
  
}

for (i in 34:48) {
  
  data_matrix_new[i,2970] <- "UC"
  
}


###########################
## Unsupervised Learning ##
###########################

## Principal Component Analysis (Numeric Variables) ##

pca1 <- prcomp(data_matrix_new[,1:2969], scale = FALSE, center = FALSE)

fviz_pca_biplot(pca1, repel = F,
                col.var = "black", # Variables color
                addEllipses = T  # add ellipse around individuals
)


## Hierarchical Clustering ##

h_clust <- eclust(data_matrix_new[,1:2969], "hclust", 3 , hc_metric = "manhattan", hc_method = "ward.D", graph = TRUE)

fviz_dend(h_clust, show_labels = TRUE, palette = "jco", as.ggplot= TRUE)


## DBSCAN ##

dbscan <- fpc::dbscan(data_matrix_new[,1:2969], eps = 2000, MinPts =3)  
fviz_cluster(dbscan, data_matrix_new[,1:2969], stand = FALSE, ellipse = TRUE, geom = "point")


## Entropy, Information Gain & Gain Ratio of variables ##

ig_entropy <- information.gain(Patient.Type~., data_matrix_new, unit = "log2")
colnames(ig_entropy) <- "Information Gain"

gr_entropy <- gain.ratio(Patient.Type~., data_matrix_new, unit = "log2")
colnames(gr_entropy) <- "Gain Ratio"


## Gini Index of variables ##

gini_ind <- as.data.frame(lapply(data_matrix_new, Gini))
gini_ind <- as.data.frame(t(gini_ind))
colnames(gini_ind) <- "Gini Index"


###################
## Decision Tree ##
###################

#------------------------------------------------
"Train-Test Data Split"
#------------------------------------------------
training_size <- 0.5 #extracting Percentage
n = nrow(data_matrix)
smp_size <- floor(training_size * n)  
index<- sample(seq_len(n),size = smp_size)

# Breaking into Training and Testing Sets:
TrainingSet <- data_matrix[index,]
TestingSet <- data_matrix[-index,]

######   or   ######  

# Choosing pre-partitioned Training Set and Testing Set of the Heart Failure Prediction Data Set #
TrainingSet = read.csv(file.choose(), header = TRUE, sep = ",")
TestingSet = read.csv(file.choose(), header = TRUE, sep = ",")

#####################

## Transform Variables ##
TrainingSet$Patient.Type <- as.factor(TrainingSet$Patient.Type)
TestingSet$Patient.Type <- as.factor(TestingSet$Patient.Type)

n <- NCOL(TrainingSet)

for (i in 1:(n-1)) {
  
  TrainingSet[,i] <- as.numeric(TrainingSet[,i])
  
}

TestingSet$Patient.Type <- as.factor(TestingSet$Patient.Type)

for (i in 1:(n-1)) {
  
  TestingSet[,i] <- as.numeric(TestingSet[,i])
  
}

##########################################
# CART #
########

options(digits.secs = 6)
start.time1 <- Sys.time()
tree1 <- rpart(Patient.Type ~.,data=TrainingSet, method = 'class', parms = list(split = "gini"))
end.time1 <- Sys.time()

rpart.plot(tree1)

start.time2 <- Sys.time()
Prediction1 <- predict(tree1, newdata=TestingSet,type = 'class')
end.time2 <- Sys.time()

# Confusion Matrix #

levels <- levels(Prediction1)
levels <- levels[order(levels)]    
cm1 <- table(ordered(Prediction1,levels), ordered(TestingSet$Patient.Type, levels))
cm1

time_taken1 <- end.time1 -start.time1
time_taken2 <- end.time2 -start.time2
time_taken1
time_taken2

###########################################

###########################################
# C4.5 #
########

options(digits.secs = 6)
start.time1 <- Sys.time()
tree2 <- J48(Patient.Type~., data = TrainingSet)
end.time1 <- Sys.time()
plot(tree2)

start.time2 <- Sys.time()
Prediction2 <- predict(tree2, newdata = TestingSet, type = "class")
end.time2 <- Sys.time()

# Confusion Matrix #

levels2 <- levels(Prediction2)
levels2 <- levels[order(levels2)]    
cm2 <- table(ordered(Prediction2,levels2), ordered(TestingSet$Patient.Type, levels2))
cm2

time_taken1 <- end.time1 -start.time1
time_taken2 <- end.time2 -start.time2
time_taken1
time_taken2

###########################################

###########################################
# C5.0 #
########

options(digits.secs = 6)
start.time1 <- Sys.time()
tree3 <- C5.0(Patient.Type~., data = TrainingSet)
end.time1 <- Sys.time()
plot(tree3)

start.time2 <- Sys.time()
Prediction3 <- predict(tree3, newdata = TestingSet, type = "class")
end.time2 <- Sys.time()

# Confusion Matrix #

levels3 <- levels(Prediction3)
levels3 <- levels[order(levels3)]    
cm3 <- table(ordered(Prediction3,levels3), ordered(TestingSet$Patient.Type, levels3))
cm3

time_taken1 <- end.time1 -start.time1
time_taken2 <- end.time2 -start.time2
time_taken1
time_taken2

###########################################