package org.example.model;

public class PlayerScore {

    // TODO: Класс является анемичной моделью — он является лишь контейнером для данных, а значительная часть логики находится в другом классе.
        // Если бы у класса были методы, совершающие необходимую работу над полями,
        // это больше соответствовало бы ООП стилю и обязанности класса (в роли доменной модели).
        // Также, эту часть логики было бы легче тестировать.
        // (см. файл "reach-anemic-model.md" в этом же пакете)

    // TODO: Сейчас класс позволяет внешнему коду установить любое значение int для счёта в гейме (например, 142 или -9).
        // Поскольку в гейме особый счёт с ограниченным набором значений, хорошим подходом было бы
        // создать специальный enum с константами ZERO, FIFTEEN, THIRTY, FORTY, ADVANTAGE для хранения счёта в гейме.
        // Это соответствовало бы ООП подходу и гарантировало, что полю счёта никогда не будет присвоено несуществующее значение.

private int points = 0;
private int games = 0;
private int sets = 0;
private Integer tieBreakPoints = null;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public Integer getTieBreakPoints() {
        return tieBreakPoints;
    }

    public void setTieBreakPoints(Integer tieBreakPoints) {
        this.tieBreakPoints = tieBreakPoints;
    }
}
