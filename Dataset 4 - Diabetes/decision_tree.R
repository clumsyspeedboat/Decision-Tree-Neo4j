################
### Diabetes ### ---> DATA ANALYSIS + DECISION TREE ALGORITHMS
################

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
options(digits.secs = 6)
data1 <- file.choose()
time1 <- Sys.time()
data_matrix <- read.csv(data1, header = TRUE, sep = ",")
time2 <- Sys.time()
time1-time2


# Transform variables
data_matrix$Diabetes_012 <- as.factor(data_matrix$Diabetes_012)
data_matrix$HighBP <- as.factor(data_matrix$HighBP)
data_matrix$HighChol <- as.factor(data_matrix$HighChol)
data_matrix$CholCheck <- as.factor(data_matrix$CholCheck)
data_matrix$BMI <- as.numeric(data_matrix$BMI)
data_matrix$Smoker <- as.factor(data_matrix$Smoker)
data_matrix$Stroke <- as.factor(data_matrix$Stroke)
data_matrix$HeartDiseaseorAttack <- as.factor(data_matrix$HeartDiseaseorAttack)
data_matrix$PhysActivity <- as.factor(data_matrix$PhysActivity)
data_matrix$Fruits <- as.factor(data_matrix$Fruits)
data_matrix$Veggies <- as.factor(data_matrix$Veggies)
data_matrix$HvyAlcoholConsump <- as.factor(data_matrix$HvyAlcoholConsump)
data_matrix$AnyHealthcare <- as.factor(data_matrix$AnyHealthcare)
data_matrix$NoDocbcCost <- as.factor(data_matrix$NoDocbcCost)
data_matrix$GenHlth <- as.factor(data_matrix$GenHlth)
data_matrix$MentHlth <- as.numeric(data_matrix$MentHlth)
data_matrix$PhysHlth <- as.numeric(data_matrix$PhysHlth)
data_matrix$DiffWalk <- as.factor(data_matrix$DiffWalk)
data_matrix$Sex <- as.factor(data_matrix$Sex)
data_matrix$Age <- as.numeric(data_matrix$Age)
data_matrix$Education <- as.factor(data_matrix$Education)
data_matrix$Income <- as.numeric(data_matrix$Income)

# sapply(data_matrix, class)

###################
## Decision Tree ##
###################

###########################################
# CART # --> Gini Index
########
  
  options(digits.secs = 6)
  start.time1 <- Sys.time()
  train.control <- trainControl(method = 'cv', number = 80)
  tree1 <- train(Diabetes_012 ~., data = data_matrix, method = "rpart", trControl = train.control, parms=list(split="gini"))
  end.time1 <- Sys.time()
  
  Prediction1 <- confusionMatrix(tree1)
  
  print(Prediction1)
  
  cf <- as.data.frame(as.table(Prediction1$table))
  
  a = sum(cf[1,3],cf[2,3],cf[3,3])
  b = sum(cf[4,3],cf[5,3],cf[6,3])
  c = sum(cf[7,3],cf[8,3],cf[9,3])
  d = sum(cf[1,3],cf[4,3],cf[7,3])
  e = sum(cf[2,3],cf[5,3],cf[8,3])
  f = sum(cf[3,3],cf[6,3],cf[9,3])
  diagonal = sum(cf[1,3],cf[5,3],cf[9,3])
  total = sum(cf$Freq)
  
  corrPred = diagonal/total
  accuracy1 = corrPred*100
  
  mccNum <- (diagonal*total)-(c*f)-(b*e)-(a*d)
  mccDen <- sqrt(total**2 - a**2 -b**2 -c**2) * sqrt(total**2 - d**2 - e**2 - f**2)
  
  mcc1 <- mccNum/mccDen
  
  time_taken1 <- as.numeric(end.time1 - start.time1) * 60
  

###########################################

###########################################
# ID3 # --> Information Gain
########

  options(digits.secs = 6)
  start.time1 <- Sys.time()
  train.control <- trainControl(method = 'cv', number = 80)
  tree2 <- train(Diabetes_012 ~., data = data_matrix, method = "rpart", trControl = train.control, parms=list(split="information"))
  end.time1 <- Sys.time()
  
  Prediction1 <- confusionMatrix(tree2)
  
  print(Prediction1)
  
  cf <- as.data.frame(as.table(Prediction1$table))
  
  a = sum(cf[1,3],cf[2,3],cf[3,3])
  b = sum(cf[4,3],cf[5,3],cf[6,3])
  c = sum(cf[7,3],cf[8,3],cf[9,3])
  d = sum(cf[1,3],cf[4,3],cf[7,3])
  e = sum(cf[2,3],cf[5,3],cf[8,3])
  f = sum(cf[3,3],cf[6,3],cf[9,3])
  diagonal = sum(cf[1,3],cf[5,3],cf[9,3])
  total = sum(cf$Freq)
  
  corrPred = diagonal/total
  accuracy2 = corrPred*100
  
  mccNum <- (diagonal*total)-(c*f)-(b*e)-(a*d)
  mccDen <- sqrt(total**2 - a**2 -b**2 -c**2) * sqrt(total**2 - d**2 - e**2 - f**2)
  
  mcc2 <- mccNum/mccDen
  
  time_taken2 <- as.numeric(end.time1 - start.time1) * 60


###########################################


###########################################
# C4.5 # --> Gain Ratio
########
  
  options(digits.secs = 6)
  start.time1 <- Sys.time()
  tree3 <- J48(Diabetes_012~., data = data_matrix)
  end.time1 <- Sys.time()
  
  Predictions1 <- evaluate_Weka_classifier(tree3, numFolds = 80, class = TRUE)
  cf <- as.data.frame(as.table(Prediction1$confusionMatrix))
  
  a = sum(cf[1,3],cf[2,3],cf[3,3])
  b = sum(cf[4,3],cf[5,3],cf[6,3])
  c = sum(cf[7,3],cf[8,3],cf[9,3])
  d = sum(cf[1,3],cf[4,3],cf[7,3])
  e = sum(cf[2,3],cf[5,3],cf[8,3])
  f = sum(cf[3,3],cf[6,3],cf[9,3])
  diagonal = sum(cf[1,3],cf[5,3],cf[9,3])
  total = sum(cf$Freq)
  
  corrPred = diagonal/total
  accuracy3 = corrPred*100
  
  mccNum <- (diagonal*total)-(c*f)-(b*e)-(a*d)
  mccDen <- sqrt(total**2 - a**2 -b**2 -c**2) * sqrt(total**2 - d**2 - e**2 - f**2)
  
  mcc3 <- mccNum/mccDen
  
  time_taken3 <- as.numeric(end.time1 - start.time1) * 60


###########################################
######################################################################################