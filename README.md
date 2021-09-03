mvn test -Dbrowser=firefox
mvn gauge:execute -DspecsDir=specs -DinParallel=true
mvn gauge:execute -DspecsDir=specs -DinParallel=true -Dbrowser=chrome