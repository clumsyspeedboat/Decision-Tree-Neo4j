###################
###### COVID ###### ---> DATA ANALYSIS + DECISION TREE ALGORITHMS
###################

# Cleaning the work space #

cat("\f")       # Clear old outputs
rm(list=ls())   # Clear all variables


if(!require("ggplot2")) install.packages("ggplot2")
if(!require("factoextra")) install.packages("factoextra")    # PCA
if(!require("FSelector")) install.packages("FSelector")
if(!require("DescTools")) install.packages("DescTools")
if(!require("rpart")) install.packages("rpart") 
if(!require("rpart.plot")) install.packages("rpart.plot")
if(!require("caret")) install.packages("caret") 
if(!require("C50")) install.packages("C50")
if(!require("RWeka")) install.packages("RWeka")
if(!require("tidyr")) install.packages("tidyr")
if(!require("dplyr")) install.packages("dplyr")


library("ggplot2")
library("factoextra")
library("FSelector")
library("DescTools")
library("rpart")
library("rpart.plot")
library("caret")
library("C50")
library("RWeka")
library("tidyr")
library("dplyr")


# Creating a data matrix #
data <- file.choose()
data_matrix <- read.csv(data, header = TRUE, sep = ",", na.strings=c("","NA"))
# data_matrix[data_matrix == ""] <- NA

# Selecting necessary columns for Decision Tree Implementation #
data_matrix <- data_matrix[,c(1,3,4,26,32,33,34,35,36,37,42,47,50)]
data_matrix <- data_matrix[,c(2,11,3,4,5,6,7,8,9,10,12,13,1)]

# Information about the variables #
info1_data <- str(data_matrix)
info_data <- as.data.frame.matrix(summary(data_matrix))

# Handling null values in the data set #
data1 <- data_matrix[,1:2]
data2 <- data_matrix[,3:13]

data1[is.na(data1)] <- 0          # for numeric variables
data2[is.na(data2)] <- "unknown"  # for categorical variables
data_matrix <- cbind(data1,data2)

# Transforming Variables #
for (i in 1:2) {
    data_matrix[,i] <- as.numeric(data_matrix[,i])
}
  
for (i in 3:13) {
  data_matrix[,i] <- as.factor(data_matrix[,i])
}


###########################
## Unsupervised Learning ##
###########################

## Principal Component Analysis (Numeric Variables) ##
pca1 <- prcomp(data_matrix[,c(2,11)], scale = TRUE, center = TRUE)
fviz_pca_biplot(pca1, repel = F,
                col.var = "black", # Variables color
                addEllipses = T  # add ellipse around individuals
)

## Hierarchical Clustering ##
h_clust <- eclust(data_matrix[,c(3,4,6,7,8,9,10,11,12,13)], "hclust", 3 , hc_metric = "manhattan", hc_method = "ward.D", graph = TRUE)
fviz_dend(h_clust, show_labels = TRUE, palette = "jco", as.ggplot= TRUE)


## DBSCAN ##
dbscan <- fpc::dbscan(data_matrix[,c(3,4,6,7,8,9,10,11,12,13)], eps = 20, MinPts =3)  
fviz_cluster(dbscan, data_matrix[,c(3,4,6,7,8,9,10,11,12,13)], stand = FALSE, ellipse = TRUE, geom = "point")


## Entropy, Information Gain & Gain Ratio of variables ##
ig_entropy <- information.gain(Diagnosis~., data_matrix, unit = "log2")
colnames(ig_entropy) <- "Information Gain"

gr_entropy <- gain.ratio(Diagnosis~. , data_matrix, unit = "log2")
colnames(gr_entropy) <- "Gain Ratio"


## Gini Index of variables ##
gini_ind <- as.data.frame(lapply(data_matrix, Gini))
gini_ind <- as.data.frame(t(gini_ind))
colnames(gini_ind) <- "Gini Index"


###################
## Decision Tree ##
###################

#------------------------------------------------
"Train-Test Data Split"
#------------------------------------------------
####################
training_size <- 0.75 #extracting Percentage
n = nrow(data_matrix)
smp_size <- floor(training_size * n)  
index<- sample(seq_len(n),size = smp_size, replace = FALSE)

#Breaking into Training and Testing Sets:
TrainingSet <- data_matrix[index,]
TestingSet <- data_matrix[-index,]

# Export CSV #
# write.csv(TrainingSet, file = "Training Set.csv", row.names = FALSE)
# write.csv(TestingSet, file = "Testing Set.csv", row.names = FALSE)

######   or   ######  

# Choosing pre-partitioned Training Set and Testing Set #
TrainingSet = read.csv(file.choose(), header = TRUE, sep = ",")
TestingSet = read.csv(file.choose(), header = TRUE, sep = ",")
#####################

# Transform Variables #

TrainingSet$Diagnosis <- as.factor(TrainingSet$Diagnosis)
for (i in 6:13) {
  TrainingSet[,i] <- as.numeric(TrainingSet[,i])
}

TestingSet$Diagnosis <- as.factor(TestingSet$Diagnosis)
for (i in 6:13) {
  TestingSet[,i] <- as.numeric(TestingSet[,i])
}

##########################################
# CART #
########

start.time <- Sys.time()

tree1 <- rpart(Diagnosis ~.,data=TrainingSet, method = 'class', parms = list(split = "gini"))
rpart.plot(tree1)

Prediction1 <- predict(tree1, newdata=TestingSet,type = 'class')

# Confusion Matrix #

levels <- levels(Prediction1)
levels <- levels[order(levels)]    
cm1 <- table(ordered(Prediction1,levels), ordered(TestingSet$Diagnosis, levels))
cm1

end.time <- Sys.time()
time_taken <- end.time -start.time

###########################################

###########################################
# C4.5 #
########

start.time <- Sys.time()

tree2 <- J48(Diagnosis~., data = TrainingSet[,c(1,2,13)])
plot(tree2)

Prediction2 <- predict(tree2, newdata = TestingSet[,c(1,2,13)], type = "class")

# Confusion Matrix #

levels2 <- levels(Prediction2)
levels2 <- levels[order(levels2)]    
cm2 <- table(ordered(Prediction2,levels2), ordered(TestingSet$Diagnosis, levels2))
cm2

end.time <- Sys.time()
time_taken <- end.time -start.time

###########################################

###########################################
# C5.0 #
########

tree3 <- C5.0(Diagnosis~., data = TrainingSet[,c(1,2,13)])
plot(tree3)

Prediction3 <- predict(tree3, newdata = TestingSet[,c(1,2,13)], type = "class")

# Confusion Matrix #

levels3 <- levels(Prediction3)
levels3 <- levels[order(levels3)]    
cm3 <- table(ordered(Prediction3,levels3), ordered(TestingSet$Diagnosis, levels3))
cm3
###########################################