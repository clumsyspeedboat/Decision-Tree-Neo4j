###############################
## Heart Failure Prediction ### ---> DATA ANALYSIS + DECISION TREE ALGORITHMS
###############################

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

###########################################
# CART # --> Gini Index
########

accuracy = vector("numeric",30)
time = vector("numeric",30)

for (i in 1:30) {
  
  options(digits.secs = 6)
  start.time1 <- Sys.time()
  train.control <- trainControl(method = 'cv', number = 20)
  tree1 <- train(DEATH_EVENT ~. ,data = data_matrix, method = "rpart", trControl = train.control, parms=list(split="gini"))
  end.time1 <- Sys.time()
  
  plot(tree1)
  
  Prediction1 <- confusionMatrix(tree1)
  
  print(Prediction1)
  
  cf <- as.data.frame(as.table(Prediction1$table))
  corrPred = sum(cf[1,3],cf[4,3])
  accuracy[i] = corrPred
  
  
  
  time_taken1 <- end.time1 -start.time1
  time_taken1
  time[i] <- time_taken1

}

sum(accuracy)/30
sum(time)/30


###########################################


###########################################
# C4.5 # --> Gain Ratio
########

accuracy = vector("numeric",30)
time = vector("numeric",30)

for (i in 1:30) {
  
options(digits.secs = 6)
start.time1 <- Sys.time()
tree2 <- J48(DEATH_EVENT~., data = data_matrix)
e <- evaluate_Weka_classifier(tree2, numFolds = 20, class = TRUE)
end.time1 <- Sys.time()


cf <- as.data.frame(as.table(e$confusionMatrix))

a <- sum(cf[1,3],cf[4,3])
b <- sum(cf[1,3],cf[2,3],cf[3,3],cf[4,3])

corrPred = (a/b)*100
accuracy[i] = corrPred


time_taken1 <- end.time1 -start.time1
time_taken1
time[i] <- time_taken1

}

sum(accuracy)/30
sum(time)/30

###########################################
######################################################################################