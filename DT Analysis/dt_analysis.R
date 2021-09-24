#########################
###### DT_ANALYSIS ######
#########################

# Cleaning the work space #
cat("\f")       # Clear old outputs
rm(list=ls())   # Clear all variables

# Creating a data matrix #
data <- file.choose()
data_matrix <- read.csv(data, header = TRUE, sep = ",", na.strings=c("","NA"))

# Data Subsets #
dt_r <- data_matrix[which(data_matrix$language == "r"),]
dt_python <- data_matrix[which(data_matrix$language == "python"),]
dt_java <- data_matrix[which(data_matrix$language == "java"),]
dt_neo_csv <- data_matrix[which(data_matrix$language == "neo4j_csv"),]
dt_neo_db <- data_matrix[which(data_matrix$language == "neo4j_db"),]

dt1 <- data_matrix[which(data_matrix$dataset == "heart failure prediction"),]
dt2 <- data_matrix[which(data_matrix$dataset == "metaprotein"),]
dt3 <- data_matrix[which(data_matrix$dataset == "flu"),]


# Accuracy #
r_gini <- mean(dt_r$accuracy[which(dt_r$splitting_criterion == "gini")])
r_gainratio <- mean(dt_r$accuracy[which(dt_r$splitting_criterion == "gain ratio")])

python_gini <- mean(dt_python$accuracy[which(dt_python$splitting_criterion == "gini")])
python_infogain <- mean(dt_python$accuracy[which(dt_python$splitting_criterion == "info gain")])

java_gini <- mean(dt_java$accuracy[which(dt_java$splitting_criterion == "gini")])
java_infogain <- mean(dt_java$accuracy[which(dt_java$splitting_criterion == "info gain")])
java_gainratio <- mean(dt_java$accuracy[which(dt_java$splitting_criterion == "gain ratio")])

neo4j_csv_gini <- mean(dt_neo_csv$accuracy[which(dt_neo_csv$splitting_criterion == "gini")])
neo4j_csv_infogain <- mean(dt_neo_csv$accuracy[which(dt_neo_csv$splitting_criterion == "info gain")])
neo4j_csv_gainratio <- mean(dt_neo_csv$accuracy[which(dt_neo_csv$splitting_criterion == "gain ratio")])

neo4j_db_gini <- mean(dt_neo_db$accuracy[which(dt_neo_db$splitting_criterion == "gini")])
neo4j_db_infogain <- mean(dt_neo_db$accuracy[which(dt_neo_db$splitting_criterion == "info gain")])
neo4j_db_gainratio <- mean(dt_neo_db$accuracy[which(dt_neo_db$splitting_criterion == "gain ratio")])

dt1_acc <- mean(dt1$accuracy)
dt2_acc <- mean(dt2$accuracy)
dt3_acc <- mean(dt3$accuracy)


# Generation Time #
r_gini <- mean(dt_r$generation_time[which(dt_r$splitting_criterion == "gini")])
r_gainratio <- mean(dt_r$generation_time[which(dt_r$splitting_criterion == "gain ratio")])

python_gini <- mean(dt_python$generation_time[which(dt_python$splitting_criterion == "gini")])
python_infogain <- mean(dt_python$generation_time[which(dt_python$splitting_criterion == "info gain")])

java_gini <- mean(dt_java$generation_time[which(dt_java$splitting_criterion == "gini")])
java_infogain <- mean(dt_java$generation_time[which(dt_java$splitting_criterion == "info gain")])
java_gainratio <- mean(dt_java$generation_time[which(dt_java$splitting_criterion == "gain ratio")])

neo4j_csv_gini <- mean(dt_neo_csv$generation_time[which(dt_neo_csv$splitting_criterion == "gini")])
neo4j_csv_infogain <- mean(dt_neo_csv$generation_time[which(dt_neo_csv$splitting_criterion == "info gain")])
neo4j_csv_gainratio <- mean(dt_neo_csv$generation_time[which(dt_neo_csv$splitting_criterion == "gain ratio")])

neo4j_db_gini <- mean(dt_neo_db$generation_time[which(dt_neo_db$splitting_criterion == "gini")])
neo4j_db_infogain <- mean(dt_neo_db$generation_time[which(dt_neo_db$splitting_criterion == "info gain")])
neo4j_db_gainratio <- mean(dt_neo_db$generation_time[which(dt_neo_db$splitting_criterion == "gain ratio")])

dt1_gt <- mean(dt1$generation_time)
dt2_gt <- mean(dt2$generation_time)
dt3_gt <- mean(dt3$generation_time)
