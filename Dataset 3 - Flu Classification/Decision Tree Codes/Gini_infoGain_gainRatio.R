################################
###### Flu Classification ###### ---> DATA ANALYSIS + DECISION TREE ALGORITHMS
################################

# Cleaning the work space #
cat("\f")       # Clear old outputs
rm(list=ls())   # Clear all variables

if(!require("ggplot2")) install.packages("ggplot2")       # Visualization
if(!require("caret")) install.packages("caret") 
if(!require("factoextra")) install.packages("factoextra") # PCA
if(!require("FSelector")) install.packages("FSelector")   # Information Gain & Gain Ratio
if(!require("DescTools")) install.packages("DescTools")   # Gini Index
if(!require("rpart")) install.packages("rpart")           # Decision Tree : CART (gini)
if(!require("rpart.plot")) install.packages("rpart.plot") # Decision Tree plot : CART
if(!require("C50")) install.packages("C50")               # Decision Tree : C 5.0 (gain ratio)
if(!require("RWeka")) install.packages("RWeka")           # Decision Tree : C 4.5 (gain ratio)

library("ggplot2")
library("caret")
library("factoextra")
library("FSelector")
library("DescTools")
library("rpart")
library("rpart.plot")
library("C50")
library("RWeka")


# Creating a data matrix #
data <- file.choose()
data_matrix <- read.csv(data, header = TRUE, sep = ",", na.strings=c("","NA"))
# data_matrix[data_matrix == ""] <- NA

# Transforming Variables #
for (i in 1:2) {
  data_matrix[,i] <- as.numeric(data_matrix[,i])
}

for (i in 3:12) {
  data_matrix[,i] <- as.factor(data_matrix[,i])
}

###################
## Data Analysis ##
###################

ggplot(data = data_matrix) +
  geom_histogram(aes(x = age), bins = 10, colour = "black", fill = "red")

ggplot(data = data_matrix) +
  geom_histogram(aes(x = creatinine_phosphokinase), bins = 10, colour = "black", fill = "red")

ggplot(data = data_matrix) +
  geom_histogram(aes(x = ejection_fraction), bins = 10, colour = "black", fill = "red")

ggplot(data = data_matrix) +
  geom_histogram(aes(x = platelets), bins = 10, colour = "black", fill = "red")

ggplot(data = data_matrix) +
  geom_histogram(aes(x = serum_creatinine), bins = 10, colour = "black", fill = "red")

ggplot(data = data_matrix) +
  geom_histogram(aes(x = serum_sodium), bins = 10, colour = "black", fill = "red")

ggplot(data = data_matrix) +
  geom_histogram(aes(x = time), bins = 10, colour = "black", fill = "red")

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

###########################################
# CART # --> Gini Index
########

accuracy = vector("numeric")
time = vector("numeric")
mcc = vector("numeric")
cf = matrix("numeric")

for (i in 1:30) {
  
  options(digits.secs = 6)
  start.time1 <- Sys.time()
  train.control <- trainControl(method = 'cv', number = 40)
  tree1 <- train(Diagnosis ~. ,data = data_matrix, method = "rpart", trControl = train.control, parms=list(split="gini"))
  end.time1 <- Sys.time()
  
  Prediction1 <- confusionMatrix(tree1)
  
  print(Prediction1)
  
  cf <- as.data.frame(as.table(Prediction1$table))
  
  tp <- cf[1,3]
  tn <- cf[4,3]
  fp <- cf[3,3]
  fn <- cf[2,3]
  
  corrPred = (tp+tn)/(tp+tn+fp+fn)
  accuracy[i] = corrPred*100
  
  mccNum <- (tp*tn)-(fp*fn)
  mccDen <- sqrt((tp+fp)*(tp+fn)*(tn+fp)*(tn+fn))
  
  numMcc <- mccNum/mccDen
  mcc[i] = numMcc
  
  time_taken1 <- end.time1 -start.time1
  time_taken1
  time[i] <- time_taken1
  
}

mean(accuracy)
mean(mcc)
mean(time)

###########################################

###########################################
# ID3 # --> Information Gain
########

accuracy = vector("numeric")
time = vector("numeric")
mcc = vector("numeric")
cf = matrix("numeric")

for (i in 1:30) {
  
  options(digits.secs = 6)
  start.time1 <- Sys.time()
  train.control <- trainControl(method = 'cv', number = 40)
  tree1 <- train(Diagnosis ~. ,data = data_matrix, method = "rpart", trControl = train.control, parms=list(split="information"))
  end.time1 <- Sys.time()
  
  Prediction1 <- confusionMatrix(tree1)
  
  print(Prediction1)
  
  cf <- as.data.frame(as.table(Prediction1$table))
  
  tp <- cf[1,3]
  tn <- cf[4,3]
  fp <- cf[3,3]
  fn <- cf[2,3]
  
  corrPred = (tp+tn)/(tp+tn+fp+fn)
  accuracy[i] = corrPred*100
  
  mccNum <- (tp*tn)-(fp*fn)
  mccDen <- sqrt((tp+fp)*(tp+fn)*(tn+fp)*(tn+fn))
  
  numMcc <- mccNum/mccDen
  mcc[i] = numMcc
  
  time_taken1 <- end.time1 -start.time1
  time_taken1
  time[i] <- time_taken1
  
}

mean(accuracy)
mean(mcc)
mean(time)

###########################################


###########################################
# C4.5 # --> Gain Ratio
########

accuracy = vector("numeric")
time = vector("numeric")
mcc = vector("numeric")
cf = matrix("numeric")

for (i in 1:30) {
  
  options(digits.secs = 6)
  start.time1 <- Sys.time()
  tree1 <- J48(Diagnosis~., data = data_matrix)
  end.time1 <- Sys.time()
  
  Predictions1 <- evaluate_Weka_classifier(tree1, numFolds = 40, class = TRUE)
  cf <- as.data.frame(as.table(Prediction1$confusionMatrix))
  
  tp <- cf[1,3]
  tn <- cf[4,3]
  fp <- cf[3,3]
  fn <- cf[2,3]
  
  corrPred = (tp+tn)/(tp+tn+fp+fn)
  accuracy[i] = corrPred*100
  
  mccNum <- (tp*tn)-(fp*fn)
  mccDen <- sqrt((tp+fp)*(tp+fn)*(tn+fp)*(tn+fn))
  
  numMcc <- mccNum/mccDen
  mcc[i] = numMcc
  
  time_taken1 <- end.time1 -start.time1
  time_taken1
  time[i] <- time_taken1
  
}

mean(accuracy)
mean(mcc)
mean(time)

###########################################
######################################################################################