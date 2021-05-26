###############################
## Heart Failure Prediction ### ---> DATA ANALYSIS + DECISION TREE ALGORITHMS
###############################

# Cleaning the work space #

cat("\f")       # Clear old outputs
rm(list=ls())   # Clear all variables


if(!require("ggplot2")) install.packages("ggplot2")
if(!require("factoextra")) install.packages("factoextra")
if(!require("FSelector")) install.packages("FSelector")
if(!require("DescTools")) install.packages("DescTools")
if(!require("rpart")) install.packages("rpart") 
if(!require("rpart.plot")) install.packages("rpart.plot")
if(!require("caret")) install.packages("caret") 
if(!require("C50")) install.packages("C50")
if(!require("RWeka")) install.packages("RWeka")


library("ggplot2")
library("factoextra")
library("FSelector")
library("DescTools")
library("rpart")
library("rpart.plot")
library("caret")
library("C50")
library("RWeka")


# Creating a data matrix #

data1 <- file.choose()
data1

data_matrix <- read.csv(data1, header = TRUE, sep = ",")


###################
## Data Analysis ##
###################

## Variable Conversion ##

data_matrix$age <- as.numeric(data_matrix$age)
data_matrix$anaemia <- as.factor(data_matrix$anaemia)
data_matrix$creatinine_phosphokinase <- as.numeric(data_matrix$creatinine_phosphokinase)
data_matrix$diabetes <- as.factor(data_matrix$diabetes)
data_matrix$ejection_fraction <- as.numeric(data_matrix$ejection_fraction)
data_matrix$high_blood_pressure <- as.factor(data_matrix$high_blood_pressure)
data_matrix$platelets <- as.numeric(data_matrix$platelets)
data_matrix$serum_creatinine <- as.numeric(data_matrix$serum_creatinine)
data_matrix$serum_sodium <- as.numeric(data_matrix$serum_sodium)
data_matrix$sex <- as.factor(data_matrix$sex)
data_matrix$smoking <- as.factor(data_matrix$smoking)
data_matrix$time <- as.numeric(data_matrix$time)
data_matrix$DEATH_EVENT <- as.factor(data_matrix$DEATH_EVENT)


## Histograms of Numeric Variables ##

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



## Box Plots of Numeric Variables ##

ggplot(data = data_matrix) +
  geom_boxplot(aes(x = age, fill = "red")) + theme(legend.position="none")

ggplot(data = data_matrix) +
  geom_boxplot(aes(x = creatinine_phosphokinase, fill = "red")) + theme(legend.position="none")

ggplot(data = data_matrix) +
  geom_boxplot(aes(x = ejection_fraction, fill = "red")) + theme(legend.position="none")

ggplot(data = data_matrix) +
  geom_boxplot(aes(x = platelets, fill = "red")) + theme(legend.position="none")

ggplot(data = data_matrix) +
  geom_boxplot(aes(x = serum_creatinine, fill = "red")) + theme(legend.position="none")

ggplot(data = data_matrix) +
  geom_boxplot(aes(x = serum_sodium, fill = "red")) + theme(legend.position="none")

ggplot(data = data_matrix) +
  geom_boxplot(aes(x = time, fill = "red")) + theme(legend.position="none")



## Bar Plot of Categorical Variables with respect to "Death Event" ##

ggplot(data = data_matrix,aes(x = anaemia, y = DEATH_EVENT)) +
  geom_bar(stat = "identity", fill = "blue") 

ggplot(data = data_matrix,aes(x = diabetes, y = DEATH_EVENT)) +
  geom_bar(stat = "identity", fill = "blue") 

ggplot(data = data_matrix,aes(x = high_blood_pressure, y = DEATH_EVENT)) +
  geom_bar(stat = "identity", fill = "blue") 

ggplot(data = data_matrix,aes(x = sex, y = DEATH_EVENT)) +
  geom_bar(stat = "identity", fill = "blue") 

ggplot(data = data_matrix,aes(x = smoking, y = DEATH_EVENT)) +
  geom_bar(stat = "identity", fill = "blue")


## Scatter Plot showing correlation between variables ##

data_matrix$DEATH_EVENT <- as.factor(data_matrix$DEATH_EVENT)

upper.panel<-function(x, y){
  points(x,y, pch=19, col=c("blue", "red")[data_matrix$DEATH_EVENT])
  r <- round(cor(x, y), digits=2)
  txt <- paste0("R = ", r)
  usr <- par("usr"); on.exit(par(usr))
  par(usr = c(0, 1, 0, 1))
  text(0.5, 0.9, txt)
}

# All Variables
pairs(data_matrix[,1:12], lower.panel = NULL, 
      upper.panel = upper.panel)

# Numeric Variables
pairs(data_matrix[,c(1,3,5,7,8,9,12)], lower.panel = NULL, 
      upper.panel = upper.panel)



###########################
## Unsupervised Learning ##
###########################

## Principal Component Analysis (Numeric Variables) ##

pca1 <- prcomp(data_matrix[,c(1,3,5,7,8,9,12)], scale = TRUE, center = TRUE)

fviz_pca_biplot(pca1, repel = F,
                habillage = data_matrix$DEATH_EVENT,
                col.var = "black", # Variables color,
                legend.title= 'Death Event', #Title of legends
                addEllipses = T  # add ellipse around individuals
)

## Principal Component Analysis (All Variables) ##

pca2 <- prcomp(data_matrix[,1:12], scale = TRUE, center = TRUE)

fviz_pca_biplot(pca2, repel = F,
                habillage = data_matrix$DEATH_EVENT,
                col.var = "black", # Variables color,
                legend.title= 'Death Event', #Title of legends
                addEllipses = T  # add ellipse around individuals
)



## Hierarchical Clustering ##

h_clust <- eclust(data_matrix, "hclust", 4 , hc_metric = "euclidean", hc_method = "ward.D", graph = TRUE)

fviz_dend(h_clust, show_labels = TRUE, palette = "jco", as.ggplot= TRUE)

print(h_clust)
summary(h_clust)


## Entropy, Information Gain & Gain Ratio of variables ##

ig_entropy <- information.gain(DEATH_EVENT~., data_matrix, unit = "log2")
colnames(ig_entropy) <- "Information Gain"

gr_entropy <- gain.ratio(DEATH_EVENT~., data_matrix, unit = "log2")
colnames(gr_entropy) <- "Gain Ratio"

## Gini Index of variables ##

gini_ind <- as.data.frame(lapply(data_matrix, Gini))
gini_ind <- as.data.frame(t(gini_ind))
colnames(gini_ind) <- "Gini Index"


###################
## Decision Tree ##
###################

# Choosing pre-partitioned Training Set and Testing Set of the Heart Failure Prediction Data Set #

TrainingSet = read.csv(file.choose(), header = TRUE, sep = ",")
TestingSet = read.csv(file.choose(), header = TRUE, sep = ",")

TrainingSet$age <- as.numeric(TrainingSet$age)
TrainingSet$anaemia <- as.factor(TrainingSet$anaemia)
TrainingSet$creatinine_phosphokinase <- as.numeric(TrainingSet$creatinine_phosphokinase)
TrainingSet$diabetes <- as.factor(TrainingSet$diabetes)
TrainingSet$ejection_fraction <- as.numeric(TrainingSet$ejection_fraction)
TrainingSet$high_blood_pressure <- as.factor(TrainingSet$high_blood_pressure)
TrainingSet$platelets <- as.numeric(TrainingSet$platelets)
TrainingSet$serum_creatinine <- as.numeric(TrainingSet$serum_creatinine)
TrainingSet$serum_sodium <- as.numeric(TrainingSet$serum_sodium)
TrainingSet$sex <- as.factor(TrainingSet$sex)
TrainingSet$smoking <- as.factor(TrainingSet$smoking)
TrainingSet$time <- as.numeric(TrainingSet$time)
TrainingSet$DEATH_EVENT <- as.factor(TrainingSet$DEATH_EVENT)


TestingSet$age <- as.numeric(TestingSet$age)
TestingSet$anaemia <- as.factor(TestingSet$anaemia)
TestingSet$creatinine_phosphokinase <- as.numeric(TestingSet$creatinine_phosphokinase)
TestingSet$diabetes <- as.factor(TestingSet$diabetes)
TestingSet$ejection_fraction <- as.numeric(TestingSet$ejection_fraction)
TestingSet$high_blood_pressure <- as.factor(TestingSet$high_blood_pressure)
TestingSet$platelets <- as.numeric(TestingSet$platelets)
TestingSet$serum_creatinine <- as.numeric(TestingSet$serum_creatinine)
TestingSet$serum_sodium <- as.numeric(TestingSet$serum_sodium)
TestingSet$sex <- as.factor(TestingSet$sex)
TestingSet$smoking <- as.factor(TestingSet$smoking)
TestingSet$time <- as.numeric(TestingSet$time)
TestingSet$DEATH_EVENT <- as.factor(TestingSet$DEATH_EVENT)


###########################################
# CART #
########

tree1 <- rpart(DEATH_EVENT~.,data=TrainingSet, method = 'class', parms = list(split = "gini"))
rpart.plot(tree1)

Prediction1 <- predict(tree1, newdata=TestingSet,type = 'class')

# Confusion Matrix #

levels <- levels(Prediction1)
levels <- levels[order(levels)]    
cm1 <- table(ordered(Prediction1,levels), ordered(TestingSet$DEATH_EVENT, levels))
cm1
###########################################


###########################################
# C4.5 #
########

tree2 <- J48(DEATH_EVENT~., data = TrainingSet)
plot(tree2)

Prediction2 <- predict(tree2, newdata = TestingSet, type = "class")

# Confusion Matrix #

levels2 <- levels(Prediction2)
levels2 <- levels[order(levels2)]    
cm2 <- table(ordered(Prediction2,levels2), ordered(TestingSet$DEATH_EVENT, levels2))
cm2
###########################################


###########################################
# C5.0 #
########

tree3 <- C5.0(DEATH_EVENT~., data = TrainingSet)
plot(tree3)

Prediction3 <- predict(tree3, newdata = TestingSet, type = "class")

# Confusion Matrix #

levels3 <- levels(Prediction3)
levels3 <- levels[order(levels3)]    
cm3 <- table(ordered(Prediction3,levels3), ordered(TestingSet$DEATH_EVENT, levels3))
cm3
###########################################
######################################################################################