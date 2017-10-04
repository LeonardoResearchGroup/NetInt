# Script for ...

# invoke with the command
# Rscript --vanilla LouvainComms.R <EdgeListCSVfile> <OutputFilename> <EdgeWeigthOption> <NodeListCSVfile> 

# The first two input arguments are mandatory
# The second argument must not include the extension
# <EdgeWeigthOption> is the name of one of the <EdgeListCSVfile> columns 


# take input arguments
args <- commandArgs(trailingOnly=TRUE)

# Test if the first two inputs are given, if not, return an error
if (length(args) < 2) {
  stop("Está ingresando una cantidad inadecuada de argumentos al script", call.=FALSE)
}

# load library
library(igraph)

# first two input arguments
edge.list.filename <- args[1]
output.filename <- args[2] #nombreArchivo <- gsub("\"", "",nombreArchivo)

# remainig input arguments
# if they are not given, they will be NA
edge.weight.option <- args[3]
node.list.filename <- args[4]

# loading edge list
edge.list <- read.csv(edge.list.filename, header=TRUE, sep="|",
                      row.names=NULL)

# loading node list, if given
if (!is.na(node.list.filename)){
  print(node.list.filename)
  node.list <- read.csv(node.list.filename, header=TRUE, sep="|",
                        row.names=NULL)
} else {
  node.list <- NULL
}

# interpreting ""Binario" edge weigth option
if(edge.weight.option == "Binario" | is.na(edge.weight.option)){
  edge.weight.option <- NULL  
}

# graph creation
G <- graph_from_data_frame(edge.list, directed = FALSE, vertices = node.list)

# community detecion using the Louvain method
tmp <- cluster_louvain(G, weights = edge.list[,edge.weight.option])
V(G)$community <- tmp$membership

# write GRAPHML output file in the current location
write_graph(G, paste0(output.filename,".graphml"), format = "graphml")

# # con deteccion
# ebc <- cluster_louvain(grafo, weights = pesos)
# V(grafo)$community <- ebc$membership
# # sin deteccion
# # V(grafo)$community <- transacciones[,2]
# cantidadComunidades <- as.vector(table(V(grafo)$community))
# 
# print(paste("Se detectaron",length(cantidadComunidades),"comunidades"))
# print("")
# print("La modularidad de la partición es:")
# print(round(ebc$modularity[length(ebc$modularity)], digits = 3) )
# print("")

# print("Tamaño de las comunidades: ")
# print("")
# 
# for(i in  1:length(cantidadComunidades)){
#   print(paste0("Comunidad ", i," : ",cantidadComunidades[i], " miembros" ))
# }

