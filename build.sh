#!/bin/bash

# Configurar JAVA_HOME para Render
export JAVA_HOME=/opt/render/project/.render/java/17
export PATH=$JAVA_HOME/bin:$PATH

# Verificar Java
echo "Java version:"
java -version

echo "JAVA_HOME: $JAVA_HOME"

# Dar permisos al wrapper de Maven
chmod +x mvnw

# Ejecutar el build
./mvnw clean install -DskipTests

echo "Build completado exitosamente"