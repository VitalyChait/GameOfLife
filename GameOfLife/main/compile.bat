javac -d outputDir --module-path ".\resources\javaFX" --add-modules javafx.fxml,javafx.controls,javafx.graphics,javafx.base .\source\module-info.java .\source\javaFiles\*.java
copy .\resources\Scene1.fxml .\outputDir\
pause