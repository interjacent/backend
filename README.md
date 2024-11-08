# interjacent 

Довольно часто возникает необходимость выбрать подходящее время для встречи небольшой группы людей. Существующие решения, в основном, зарубежные, довольно громоздкие и содержат много лишней функциональности. Interjacent призван решить эту проблему посредством создания простых опросов для выявления временного интервала, удобного всем участникам встречи.

Interjacent не требует регистрации, что упрощает пользование и делает привлекательным для новых пользователей. Для определения подходящего времени проведения встречи Interjacent полагается на пересечение интервалов свободного времени, что позволяет быстро определять удобное для всех время встречи. Помимо этого, Interjacent показывает, в какие дни у участников больше свободного времени, в какие — меньше. Ориентируясь на эту информацию можно выбрать подходящую длину встречи, например для срочных можно пожертвовать длиной, а для некоторых важных можно, наоборот, выбрать более продолжительную встречу.

[Слайды](https://docs.google.com/presentation/d/14M_EeeYo0_RwzjTfn3l_jxKAGqeZJJv80MuBQNnnIzk/edit?usp=sharing)

# Сборка и запуск

Для сборки необходимо:

- Склонировать текущий репозиторий;

```bash
git clone https://github.com/interjacent/backend
```

- Установить Java 17 или более новую версию при её отсутствии;

- Запустить для сборки команду:

```shell
./gradlew bootJar
```

- Запустить полученный jar-файл, находящийся в директории `build/libs`.

Вы также можете пропустить два предыдущих шага и сразу запустить проект командой:

```shell
./gradlew bootRun
```

После вышеуказанных действий будет запущен HTTP-сервер на порте 8080 по умолчанию.
