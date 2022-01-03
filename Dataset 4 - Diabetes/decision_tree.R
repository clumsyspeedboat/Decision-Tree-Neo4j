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

data1 <- file.choose()
data1

data_matrix <- read.csv(data1, header = TRUE, sep = ",")


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

sapply(data_matrix, class)

n = nrow(data_matrix)
trainIndex = sample(1:n, size = round(0.75*n), replace=FALSE)
train = data_matrix[trainIndex ,]
test = data_matrix[-trainIndex ,]



start.time1 <- Sys.time()
# train.control <- trainControl(method = 'cv', number = 20)
tree1 <- train(Diabetes_012 ~. ,data = train, method = "rpart", parms=list(split="gini"))
end.time1 <- Sys.time()

plot(tree1)
rpart.plot(tree1)
fancyRpartPlot(tree1)

Prediction1 <- predict(tree1, test[2:22])

print(Prediction1)

test$predictions <- Prediction1
cf <- table(test$predictions, test$Diabetes_012)
cf <- as.matrix(cf)
cf <- as.data.frame(cf)

a = sum(as.numeric(cf[1,3],cf[2,3],cf[3,3]))
b = sum(as.numeric(cf[4,3],cf[5,3],cf[6,3]))
c = sum(as.numeric(cf[7,3],cf[8,3],cf[9,3]))
d = sum(as.numeric(cf[1,3],cf[4,3],cf[7,3]))
e = sum(as.numeric(cf[2,3],cf[5,3],cf[8,3]))
f = sum(as.numeric(cf[3,3],cf[6,3],cf[9,3]))
diagonal = sum(as.numeric(cf[1,3],cf[5,3],cf[9,3]))
total = sum(as.numeric(cf$Freq))

corrPred = diagonal/total
accuracy = corrPred*100

mccNum <- diagonal*total-c*f-b*e-a*d
mccDen <- sqrt(total**2 - a**2 -b**2 -c**2) * sqrt(total**2 - d**2 - e**2 - f**2)

numMcc <- mccNum/mccDen

