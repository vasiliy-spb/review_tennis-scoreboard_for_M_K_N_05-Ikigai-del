# Роадмап рефакторинга по файлам

Это упорядоченный список файлов, которые следует исправлять в соответствии с замечаниями в комментариях. Рекомендую двигаться последовательно.

Файлы, не указанные в списке, можно исправлять в любом порядке.

### Шаг 1: Entity и слой доступа к данным

- `/entity/Player.java`
- `/entity/Match.java`
- `/dao/PlayerDao.java`
- `/dao/PlayerDaoImpl.java`
- `/dao/MatchDao.java`
- `/dao/MatchDaoImpl.java`

### Шаг 2: Доменные модели

- `/model/PlayerScore.java`
- `/model/MatchScore.java`

### Шаг 3: Сервисный слой

- `/service/PlayerService.java`
- `/service/OngoingMatchesService.java`
- `/service/MatchService.java`

### Шаг 4: Контроллер

- `/controller/MatchController.java`

### Шаг 5: Конфигурация, мапперы, валидаторы, обработка исключений

- `/dto/MatchScoreMapper.java`
- `/util/HibernateUtil.java`

### Шаг 6: Тесты

- `/test/java/org/example/model/MatchScoreTest.java`
