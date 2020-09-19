
Предполагается, что программа должна быть запущена как минимум с тремя аргументами (вид данных, выходной файл,
входной файл).
В соответствии с заданием:

*Параметры программы задаются при запуске через аргументы командной строки, по порядку:
*1. режим сортировки (-a или -d), необязательный, по умолчанию сортируем по возрастанию;
*2. тип данных (-s или -i), обязательный;
*3. имя выходного файла, обязательное;
*4. остальные параметры – имена входных файлов, не менее одного.*

Пример входных параметров:
сортировка чисел по возрастанию с одним входным файлом
-a -i test\\out.txt test\\in1.txt;
параметр "-a" не обязательный. По умолчанию сортировка проводится по возрастанию:
-i test\\out.txt test\\in1.txt;

Log-файл LogFile.txt находится в корне программы

Заметки:
1) Я мог бы использовать PriorityQueue с peek() и poll() для решения этой задачи (PriorityQueue была бы вместо
    массива, в который я записываю Scanner или BufferedReader. Я посчитал, что это будет не совсем "честно" для решения
    этой задачи
2) Я попробовал провести имитацию слияния файлов, которые не помещаются в оперативную память. В качестве считывателя
    выступали Scanner либо BufferedReader (у него буффер в 8KB, у сканнера - 1KB). Проводил слияние файлов (один из
    которых был ~200 MB) с разными настройками Maximum Heap Size. Получилось, что оба варианта работало как при 256MB
    heap size, так и при 2GB. Программа выполнялась за ~24 секунды для Scanner и за ~19 секунд для BufferedReader.
    Поэтому остановился на нём.
3) В заднии написано  "Считается, что файлы предварительно отсортированы. Результатом работы программы должен являться
    новый файл с объединенным содержимым входных файлов, отсортированным по возрастанию или убыванию путем сортировки
    слиянием." Я это интерпретировал следующим образом: входной файл всегда отсортирован по возрастанию и в зависи-
    мости от входных аргументов мы его записываем в возрастающем или убывающем порядке (во втором случае придется
    читать файл с конца). В нашей задаче предположим, что все входные файлы отсортированы по возрастанию (и если нам
    требуется совершить сортировку по убыванию, мы эти файлы читаем с конца).
