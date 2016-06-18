datosCompletos <- read.csv("Base_Comunidades_anon.csv", header=TRUE, sep=";",
    row.names=NULL);
datosPagadores <- datosCompletos[,-c(10:21)];
datosPagadoresUnicos <- unique(datosPagadores);

colnames(datosPagadoresUnicos) <- c("ID_NIT", "CALIFICACION_INTERNA",
    "SEGMENTO", "COD_GERENTE_ASESOR", "REGION", "CIIU", "SECOR",
    "SUBCIIU", "SUBSECTOR");

datosBeneficiarios <- datosCompletos[,-c(1:9,13,20,21)];
datosBeneficiariosUnicos <- unique(datosBeneficiarios);

colnames(datosBeneficiariosUnicos) <- c("ID_NIT", "CALIFICACION_INTERNA",
    "SEGMENTO", "COD_GERENTE_ASESOR", "REGION", "CIIU", "SECOR",
    "SUBCIIU", "SUBSECTOR");
    
datosEmpresas <- rbind(datosPagadoresUnicos,datosBeneficiariosUnicos);

datosEmpresasUnicos <- unique(datosEmpresas);

write.csv2(datosEmpresasUnicos,'empresas.csv');