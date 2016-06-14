library(kohonen)

setwd("C://Users//Fell//Desktop")
data <- read.csv2("sectores.csv");
data$X <- NULL
data$CANTIDAD_TRNS <- NULL
data$VALORES_MOVILIZADOS <- NULL

pagadores <- unique(data$SECTOR_PAGADOR)
beneficiarios <- unique(data$SECTOR_BENEFICIARIO)

#Se crea la matriz de ceros
data_train_matrix <- matrix(0, nrow = length(pagadores), ncol = length(beneficiarios))

rownames(data_train_matrix) <- pagadores
colnames(data_train_matrix) <- beneficiarios

dataset.size <- nrow(data)

for(i in 1:dataset.size) {
  
  row <- data[i,]
  
  pagador <- as.character(row$SECTOR_PAGADOR)
  beneficiario <- as.character(row$SECTOR_BENEFICIARIO)
  
  pagador.position <- match(pagador, pagadores)
  beneficiario.position <- match(beneficiario, beneficiarios)
  
  data_train_matrix[pagador.position,beneficiario.position] <- 1
}

write.table(data_train_matrix, file="mymatrix.txt", row.names=FALSE, col.names=FALSE)

som_grid <- somgrid(xdim = 9, ydim=9, topo="hexagonal")

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
