library(kohonen)

setwd("C://Users//Fell//OneDrive//Icesi//Maestría//Semestre 1//Machine Learning//Proyecto del Curso//ML - Communities//Matrix_2016_06_17_06_13_27")

#Se obtiene la matriz.
my.matrix <- "Matrix_2016_06_17_06_13_27.txt"
data_train_matrix <- as.matrix(read.table(file = my.matrix, header = FALSE))

som_grid <- somgrid(xdim = 10, ydim=10, topo="hexagonal")

som_model <- som(data_train_matrix, grid=som_grid, rlen=100, alpha=c(0.05,0.01), keep.data = TRUE, n.hood="square")

plot(som_model, type="count")

som_cluster <- cutree(hclust(dist(som_model$codes)), 6)

pretty_palette <- c("#1f77b4", '#ff7f0e', '#2ca02c', '#d62728', '#9467bd', '#8c564b', '#e377c2')
plot(som_model, type="mapping", bgcol = pretty_palette[som_cluster], main = "Clusters") 
add.cluster.boundaries(som_model, som_cluster)

plot(som_model, type="changes")

plot(som_model, type="dist.neighbours")

summary(som_model)

head.matrix(data_train_matrix)
