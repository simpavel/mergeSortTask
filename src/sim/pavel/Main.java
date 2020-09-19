package sim.pavel;

import java.util.Arrays;

import static sim.pavel.MyLogger.log;

public class Main {
    //TODO bufferedReader - убираем классы со сканнером, делаем bufferedReader. Почему файл через бридер весит меньше??
    //TODO запретить использование строк, если выбран параметр (тип для строк пофиг, а для цифр сделаем) - с помощью запрета в hasNext
    //TODO тесты для FileContentBufferedReaderScanner
    //TODO сделать !инструкцию по запуску для задания
    //TODO а что если нам надо сделатьь убывающую сортировку на НЕ убывающем файле

    //TODO в заднии написано  Считается, что файлы предварительно отсортированы.
    ////TODO Результатом работы программы должен являться новый файл с объединенным содержимым
    ////TODO входных файлов, отсортированным по возрастанию или убыванию путем сортировки слиянием.
    // Это можно интерпретировать двумя способами: либо входной файл отсортирован по возрастанию, либо по убыванию
    // В нашей задаче предположим, что все входные файлы отсортированы по возрастанию (и если нам требуется совершить
    // сортировку по убыванию, мы эти файлы читаем с конца). 

    public static void main(String[] args) {

        long programStart = System.currentTimeMillis();
        System.out.println("Logger name: " + log().getName() + "\nProgram starts with arguments: " + Arrays.toString(args));

        Properties properties = Properties.fromArgs(args);

        FileMerger fileMerger = new FileMerger(properties);
        fileMerger.writeMergedFiles(properties.getOutputFileName()); //производим слияние

        System.out.println("Program took " + ((System.currentTimeMillis() - programStart) / 1000) + " seconds to " +
                "execute. \n");
    }
}
