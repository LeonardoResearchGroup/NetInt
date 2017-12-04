#!/usr/bin/env Rscript
args = commandArgs(trailingOnly=TRUE)
# test if there is at least one argument: if not, return an error
if (length(args) != 4 & length(args) != 3 ) {
  stop("Está ingresando una cantidad inadecuada de argumentos al script", call.=FALSE)
}
formaMatrizAdyacencia <- args[3]


nombreArchivo <- args[1]
nombreArchivoNodos <- args[4]
nombreArchivoDestino <- args[2]



if(is.na(nombreArchivoNodos)){
  nombreArchivoNodos <- ""
}

nombreArchivo <- gsub("\"", "",nombreArchivo)

library(igraph);
transacciones <- read.csv(nombreArchivo, header=TRUE, sep="|",
                          row.names=NULL)

nodos <- NULL

if(!is.null(nombreArchivoNodos) & nombreArchivoNodos != ""){
nodos <- read.csv(nombreArchivoNodos, header=TRUE, sep="|",
                  row.names=NULL)
}
pesos <- NULL
if(formaMatrizAdyacencia!="Binario"){
  pesos <- transacciones[,formaMatrizAdyacencia]  
}



grafo <- graph.data.frame(transacciones, directed=FALSE, vertices = nodos)
#Deletes any vertex withtout edeges
grafo <- delete.vertices(grafo,degree(grafo) == 0)
ebc <- cluster_louvain(grafo, weights = pesos)
V(grafo)$community <- ebc$membership
cantidadComunidades <- as.vector(table(V(grafo)$community))

print(paste("Se detectaron",length(cantidadComunidades),"comunidades"))
print("")
print("La modularidad de la partición es:")
print(round(ebc$modularity[length(ebc$modularity)], digits = 3) )
print("")

write.graph(grafo, nombreArchivoDestino, format="graphml")




print("Tamaño de las comunidades: ")
print("")

for(i in  1:length(cantidadComunidades)){
  print(paste0("Comunidad ", i," : ",cantidadComunidades[i], " miembros" ))
}

