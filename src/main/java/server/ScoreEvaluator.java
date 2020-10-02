package server;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import model.Die;
import model.DieFace;
import model.FortuneCardType;
import model.Turn;

final class ScoreEvaluator {
	private final Turn turn;

	private Integer points = 0;

	private Integer numberOfParrots = 0;
	private Integer numberOfSwords = 0;
	private Integer numberOfCoins = 0;
	private Integer numberOfMonkeys = 0;
	private Integer numberOfDiamonds = 0;

	public ScoreEvaluator(Turn turn) {
		this.turn = turn;
	}

	public Integer evaluate() {
		if (turn.getIsIslandOfSkulls()) {
			Integer numberOfSkulls = countDice(turn.getDice().getAll(), DieFace.SKULL)
					+ (turn.getFortuneCard().getNumericalValue());

			return numberOfSkulls * 100;
		}

		countDiceTypes();

		if (turn.getFortuneCard().getType() == FortuneCardType.MONKEY_BUSINESS) {
			points += evaluateOfAKindPoints(numberOfParrots + numberOfMonkeys);
		} else {
			points += evaluateOfAKindPoints(numberOfParrots);
			points += evaluateOfAKindPoints(numberOfMonkeys);
		}

		points += evaluateOfAKindPoints(numberOfSwords);
		points += evaluateOfAKindPoints(numberOfCoins);
		points += evaluateOfAKindPoints(numberOfDiamonds);

		points += numberOfCoins * 100;
		points += numberOfDiamonds * 100;

		if (isFullChest()) {
			points += 500;
		}

		if (turn.getFortuneCard().getType() == FortuneCardType.CAPTAIN) {
			points *= 2;
		}

		return points;
	}

	private Boolean hasTreasureChest() {
		return turn.getFortuneCard().getType() == FortuneCardType.TREASURE_CHEST;
	}

	private void countDiceTypes() {
		List<Die> validDice = diceToScore();

		numberOfParrots = countDice(validDice, DieFace.PARROT);
		numberOfSwords = countDice(validDice, DieFace.SWORD);
		numberOfCoins = countDice(validDice, DieFace.COIN);
		numberOfMonkeys = countDice(validDice, DieFace.MONKEY);
		numberOfDiamonds = countDice(validDice, DieFace.DIAMOND);

		if (turn.getFortuneCard().getType() == FortuneCardType.GOLD && numberOfCoins != 8) {
			numberOfCoins++;
		}

		if (turn.getFortuneCard().getType() == FortuneCardType.DIAMOND && numberOfDiamonds != 8) {
			numberOfDiamonds++;
		}
	}

	private Integer evaluateOfAKindPoints(Integer count) {
		if (count == 8) {
			return 4000;
		} else if (count == 7) {
			return 2000;
		} else if (count == 6) {
			return 1000;
		} else if (count == 5) {
			return 500;
		} else if (count == 4) {
			return 200;
		} else if (count == 3) {
			return 100;
		}

		return 0;
	}

	private Boolean isFullChest() {
		if (diceToScore().size() < 8) {
			return false;
		}

		return diceToScore().stream().filter(die -> !dieHasFaceValue(die)).allMatch(die -> dieIsPartOfSet(die));
	}

	private boolean dieHasFaceValue(Die die) {
		return die.getFace() == DieFace.COIN || die.getFace() == DieFace.DIAMOND;
	}

	private Boolean dieIsPartOfSet(Die die) {
		return diceToScore().stream().filter(otherDie -> diceHaveMatchingScorableFaces(die, otherDie)).count() > 2;
	}

	private Boolean diceHaveMatchingScorableFaces(Die first, Die second) {
		if (turn.getFortuneCard().getType() != FortuneCardType.MONKEY_BUSINESS) {
			return first.getFace() == second.getFace();
		}

		return dieIsMonkeyOrParrot(first) ? dieIsMonkeyOrParrot(second) : false;
	}

	private Boolean dieIsMonkeyOrParrot(Die die) {
		return die.getFace() == DieFace.MONKEY || die.getFace() == DieFace.PARROT;
	}

	private Integer countDice(List<Die> input, DieFace face) {
		return filterByPredicate(input, die -> die.getFace() == face).size();
	}

	private List<Die> diceToScore() {
		if (hasTreasureChest() && turn.isDisqualified()) {
			return turn.getDice().getAllFromTreasureChest();
		}

		return turn.getDice().getScorable();
	}

	private <T, X> List<X> filterByPredicate(List<X> input, Predicate<? super X> predicate) {
		return input.stream().filter(predicate).collect(Collectors.toList());
	}

	public <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
}
