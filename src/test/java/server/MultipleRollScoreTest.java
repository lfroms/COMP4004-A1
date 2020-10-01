package server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import model.DieFace;
import model.FortuneCard;
import model.FortuneCardType;
import model.Turn;

public class MultipleRollScoreTest {
	@Test
	public void testTurnCanContinueIsFalseAfterSkullsMultipleRollsA() throws Exception {
		DieFace[][] rollSequence = new DieFace[][] {
				{ DieFace.SKULL, DieFace.PARROT, DieFace.PARROT, DieFace.PARROT, DieFace.PARROT, DieFace.SWORD,
						DieFace.SWORD, DieFace.SWORD },
				{ DieFace.SKULL, DieFace.COIN, DieFace.COIN, DieFace.COIN, DieFace.PARROT, DieFace.SKULL, DieFace.SKULL,
						DieFace.SWORD } };

		Turn turn = new Turn(new FortuneCard(FortuneCardType.GOLD), rollSequence);

		assertFalse(turn.isDisqualified());

		turn.getDice().getAll().stream().filter(die -> die.getFace() == DieFace.PARROT)
				.forEach(die -> die.setHeld(true));

		turn.rollDice();
		assertTrue(turn.isDisqualified());
	}

	@Test
	public void testTurnCanContinueIsFalseAfterSkullsMultipleRollsB() throws Exception {
		DieFace[][] rollSequence = new DieFace[][] {
				{ DieFace.SKULL, DieFace.SKULL, DieFace.PARROT, DieFace.PARROT, DieFace.PARROT, DieFace.PARROT,
						DieFace.SWORD, DieFace.SWORD },
				{ DieFace.SKULL, DieFace.SKULL, DieFace.PARROT, DieFace.PARROT, DieFace.PARROT, DieFace.PARROT,
						DieFace.SKULL, DieFace.SWORD } };

		Turn turn = new Turn(new FortuneCard(FortuneCardType.GOLD), rollSequence);

		assertFalse(turn.isDisqualified());

		turn.getDice().getAll().stream().filter(die -> die.getFace() == DieFace.PARROT)
				.forEach(die -> die.setHeld(true));

		turn.rollDice();
		assertTrue(turn.isDisqualified());
	}

	@Test
	public void testTurnCanContinueIsFalseAfterSkullsMultipleRollsC() throws Exception {
		DieFace[][] rollSequence = new DieFace[][] {
				{ DieFace.SKULL, DieFace.PARROT, DieFace.PARROT, DieFace.PARROT, DieFace.PARROT, DieFace.SWORD,
						DieFace.SWORD, DieFace.SWORD },
				{ DieFace.SKULL, DieFace.COIN, DieFace.COIN, DieFace.COIN, DieFace.PARROT, DieFace.SKULL,
						DieFace.MONKEY, DieFace.MONKEY },
				{ DieFace.SKULL, DieFace.COIN, DieFace.COIN, DieFace.COIN, DieFace.PARROT, DieFace.SKULL, DieFace.SKULL,
						DieFace.MONKEY } };

		Turn turn = new Turn(new FortuneCard(FortuneCardType.GOLD), rollSequence);

		assertFalse(turn.isDisqualified());

		turn.getDice().getAll().stream().filter(die -> die.getFace() == DieFace.PARROT)
				.forEach(die -> die.setHeld(true));

		turn.rollDice();
		assertFalse(turn.isDisqualified());

		turn.rollDice();

		assertTrue(turn.isDisqualified());
	}

	@Test
	public void testThirdMonkeyOnSecondRoll() throws Exception {
		DieFace[][] rollSequence = new DieFace[][] {
				{ DieFace.SKULL, DieFace.SKULL, DieFace.PARROT, DieFace.PARROT, DieFace.MONKEY, DieFace.MONKEY,
						DieFace.SWORD, DieFace.SWORD },
				{ DieFace.SKULL, DieFace.SKULL, DieFace.PARROT, DieFace.PARROT, DieFace.MONKEY, DieFace.MONKEY,
						DieFace.MONKEY, DieFace.SWORD } };

		FortuneCard fortuneCard = new FortuneCard(FortuneCardType.GOLD);
		Turn turn = new Turn(fortuneCard, rollSequence);

		turn.rollDice();

		ScoreEvaluator evaluator = new ScoreEvaluator(turn.getDice(), fortuneCard, turn.isDisqualified());
		assertEquals(Integer.valueOf(200), evaluator.evaluate());
	}

	@Test
	public void tesetTwoSetsOfThreeUsingTwoRolls() throws Exception {
		DieFace[][] rollSequence = new DieFace[][] {
				{ DieFace.MONKEY, DieFace.MONKEY, DieFace.MONKEY, DieFace.PARROT, DieFace.SKULL, DieFace.SKULL,
						DieFace.SWORD, DieFace.SWORD },
				{ DieFace.MONKEY, DieFace.MONKEY, DieFace.MONKEY, DieFace.PARROT, DieFace.SKULL, DieFace.SKULL,
						DieFace.PARROT, DieFace.PARROT } };

		FortuneCard fortuneCard = new FortuneCard(FortuneCardType.GOLD);
		Turn turn = new Turn(fortuneCard, rollSequence);

		turn.getDice().getAll().get(0).setHeld(true);
		turn.getDice().getAll().get(1).setHeld(true);
		turn.getDice().getAll().get(2).setHeld(true);

		// First roll is done automatically
		turn.rollDice();

		ScoreEvaluator evaluator = new ScoreEvaluator(turn.getDice(), fortuneCard, turn.isDisqualified());
		assertEquals(Integer.valueOf(300), evaluator.evaluate());
	}

	@Test
	public void testThreeCoinsFourSwordsOverSeveralRollsGoldFortuneCard() throws Exception {
		DieFace[][] rollSequence = new DieFace[][] {
				{ DieFace.COIN, DieFace.COIN, DieFace.COIN, DieFace.PARROT, DieFace.PARROT, DieFace.PARROT,
						DieFace.SWORD, DieFace.SWORD },
				{ DieFace.COIN, DieFace.COIN, DieFace.COIN, DieFace.SWORD, DieFace.SWORD, DieFace.PARROT, DieFace.SWORD,
						DieFace.SWORD },
				{ DieFace.COIN, DieFace.COIN, DieFace.COIN, DieFace.SWORD, DieFace.SWORD, DieFace.SWORD, DieFace.SWORD,
						DieFace.PARROT } };

		FortuneCard fortuneCard = new FortuneCard(FortuneCardType.GOLD);
		Turn turn = new Turn(fortuneCard, rollSequence);

		turn.getDice().getAll().get(0).setHeld(true);
		turn.getDice().getAll().get(1).setHeld(true);
		turn.getDice().getAll().get(2).setHeld(true);

		// First roll is done automatically
		turn.rollDice();
		turn.rollDice();

		ScoreEvaluator evaluator = new ScoreEvaluator(turn.getDice(), fortuneCard, turn.isDisqualified());
		assertEquals(Integer.valueOf(800), evaluator.evaluate());
	}

	@Test
	public void testThreeCoinsFourSwordsOverSeveralRollsCaptainFortuneCard() throws Exception {
		DieFace[][] rollSequence = new DieFace[][] {
				{ DieFace.COIN, DieFace.COIN, DieFace.COIN, DieFace.PARROT, DieFace.PARROT, DieFace.PARROT,
						DieFace.SWORD, DieFace.SWORD },
				{ DieFace.COIN, DieFace.COIN, DieFace.COIN, DieFace.SWORD, DieFace.SWORD, DieFace.PARROT, DieFace.SWORD,
						DieFace.SWORD },
				{ DieFace.COIN, DieFace.COIN, DieFace.COIN, DieFace.SWORD, DieFace.SWORD, DieFace.SWORD, DieFace.SWORD,
						DieFace.PARROT } };

		FortuneCard fortuneCard = new FortuneCard(FortuneCardType.CAPTAIN);
		Turn turn = new Turn(fortuneCard, rollSequence);

		turn.getDice().getAll().get(0).setHeld(true);
		turn.getDice().getAll().get(1).setHeld(true);
		turn.getDice().getAll().get(2).setHeld(true);

		// First roll is done automatically
		turn.rollDice();
		turn.rollDice();

		ScoreEvaluator evaluator = new ScoreEvaluator(turn.getDice(), fortuneCard, turn.isDisqualified());
		assertEquals(Integer.valueOf(1200), evaluator.evaluate());
	}

	@Test
	public void testSetOfFiveSwordsOverThreeRolls() throws Exception {
		DieFace[][] rollSequence = new DieFace[][] {
				{ DieFace.COIN, DieFace.COIN, DieFace.COIN, DieFace.PARROT, DieFace.PARROT, DieFace.PARROT,
						DieFace.SWORD, DieFace.SWORD },
				{ DieFace.COIN, DieFace.COIN, DieFace.COIN, DieFace.COIN, DieFace.COIN, DieFace.SWORD, DieFace.SWORD,
						DieFace.SWORD },
				{ DieFace.SKULL, DieFace.SKULL, DieFace.PARROT, DieFace.SWORD, DieFace.SWORD, DieFace.SWORD,
						DieFace.SWORD, DieFace.SWORD } };

		FortuneCard fortuneCard = new FortuneCard(FortuneCardType.GOLD);
		Turn turn = new Turn(fortuneCard, rollSequence);

		turn.getDice().getAll().get(6).setHeld(true);
		turn.getDice().getAll().get(7).setHeld(true);

		// First roll is done automatically
		turn.rollDice();

		turn.getDice().getAll().get(5).setHeld(true);

		turn.rollDice();

		ScoreEvaluator evaluator = new ScoreEvaluator(turn.getDice(), fortuneCard, turn.isDisqualified());
		assertEquals(Integer.valueOf(600), evaluator.evaluate());
	}

	@Test
	public void testEightMonkeysOverSeveralRolls() throws Exception {
		DieFace[][] rollSequence = new DieFace[][] {
				{ DieFace.MONKEY, DieFace.COIN, DieFace.COIN, DieFace.PARROT, DieFace.PARROT, DieFace.PARROT,
						DieFace.SWORD, DieFace.SWORD },
				{ DieFace.MONKEY, DieFace.MONKEY, DieFace.COIN, DieFace.PARROT, DieFace.PARROT, DieFace.PARROT,
						DieFace.SWORD, DieFace.SWORD },
				{ DieFace.MONKEY, DieFace.MONKEY, DieFace.MONKEY, DieFace.PARROT, DieFace.PARROT, DieFace.PARROT,
						DieFace.SWORD, DieFace.SWORD },
				{ DieFace.MONKEY, DieFace.MONKEY, DieFace.MONKEY, DieFace.MONKEY, DieFace.MONKEY, DieFace.MONKEY,
						DieFace.MONKEY, DieFace.MONKEY } };

		FortuneCard fortuneCard = new FortuneCard(FortuneCardType.GOLD);
		Turn turn = new Turn(fortuneCard, rollSequence);

		turn.getDice().getAll().get(0).setHeld(true);

		// First roll is done automatically
		turn.rollDice();

		turn.getDice().getAll().get(1).setHeld(true);
		turn.rollDice();

		turn.getDice().getAll().get(2).setHeld(true);
		turn.rollDice();

		ScoreEvaluator evaluator = new ScoreEvaluator(turn.getDice(), fortuneCard, turn.isDisqualified());
		assertEquals(Integer.valueOf(4600), evaluator.evaluate());
	}

	@Test
	public void testTwoDiamondsOverTwoRolls() throws Exception {
		DieFace[][] rollSequence = new DieFace[][] {
				{ DieFace.DIAMOND, DieFace.COIN, DieFace.COIN, DieFace.PARROT, DieFace.PARROT, DieFace.PARROT,
						DieFace.SWORD, DieFace.SWORD },
				{ DieFace.DIAMOND, DieFace.DIAMOND, DieFace.SKULL, DieFace.PARROT, DieFace.SKULL, DieFace.PARROT,
						DieFace.SWORD, DieFace.SWORD } };

		FortuneCard fortuneCard = new FortuneCard(FortuneCardType.DIAMOND);
		Turn turn = new Turn(fortuneCard, rollSequence);

		turn.getDice().getAll().get(0).setHeld(true);

		// First roll is done automatically
		turn.rollDice();

		ScoreEvaluator evaluator = new ScoreEvaluator(turn.getDice(), fortuneCard, turn.isDisqualified());
		assertEquals(Integer.valueOf(400), evaluator.evaluate());
	}

	@Test
	public void testThreeDiamondsOverTwoRolls() throws Exception {
		DieFace[][] rollSequence = new DieFace[][] {
				{ DieFace.DIAMOND, DieFace.COIN, DieFace.COIN, DieFace.PARROT, DieFace.PARROT, DieFace.PARROT,
						DieFace.SWORD, DieFace.SWORD },
				{ DieFace.DIAMOND, DieFace.DIAMOND, DieFace.DIAMOND, DieFace.PARROT, DieFace.SKULL, DieFace.PARROT,
						DieFace.SWORD, DieFace.SWORD } };

		FortuneCard fortuneCard = new FortuneCard(FortuneCardType.GOLD);
		Turn turn = new Turn(fortuneCard, rollSequence);

		turn.getDice().getAll().get(0).setHeld(true);

		// First roll is done automatically
		turn.rollDice();

		ScoreEvaluator evaluator = new ScoreEvaluator(turn.getDice(), fortuneCard, turn.isDisqualified());
		assertEquals(Integer.valueOf(500), evaluator.evaluate());
	}

	@Test
	public void testThreeCoinsOverTwoRollsGoldFortuneCard() throws Exception {
		DieFace[][] rollSequence = new DieFace[][] {
				{ DieFace.COIN, DieFace.COIN, DieFace.PARROT, DieFace.PARROT, DieFace.PARROT, DieFace.PARROT,
						DieFace.SWORD, DieFace.SWORD },
				{ DieFace.COIN, DieFace.COIN, DieFace.COIN, DieFace.PARROT, DieFace.SKULL, DieFace.PARROT,
						DieFace.SWORD, DieFace.SWORD } };

		FortuneCard fortuneCard = new FortuneCard(FortuneCardType.GOLD);
		Turn turn = new Turn(fortuneCard, rollSequence);

		turn.getDice().getAll().get(0).setHeld(true);
		turn.getDice().getAll().get(1).setHeld(true);

		// First roll is done automatically
		turn.rollDice();

		ScoreEvaluator evaluator = new ScoreEvaluator(turn.getDice(), fortuneCard, turn.isDisqualified());
		assertEquals(Integer.valueOf(600), evaluator.evaluate());
	}

	@Test
	public void testThreeCoinsOverTwoRollsDiamondFortuneCard() throws Exception {
		DieFace[][] rollSequence = new DieFace[][] {
				{ DieFace.COIN, DieFace.COIN, DieFace.PARROT, DieFace.PARROT, DieFace.PARROT, DieFace.PARROT,
						DieFace.SWORD, DieFace.SWORD },
				{ DieFace.COIN, DieFace.COIN, DieFace.COIN, DieFace.PARROT, DieFace.SKULL, DieFace.PARROT,
						DieFace.SWORD, DieFace.SWORD } };

		FortuneCard fortuneCard = new FortuneCard(FortuneCardType.DIAMOND);
		Turn turn = new Turn(fortuneCard, rollSequence);

		turn.getDice().getAll().get(0).setHeld(true);
		turn.getDice().getAll().get(1).setHeld(true);

		// First roll is done automatically
		turn.rollDice();

		ScoreEvaluator evaluator = new ScoreEvaluator(turn.getDice(), fortuneCard, turn.isDisqualified());
		assertEquals(Integer.valueOf(500), evaluator.evaluate());
	}

	@Test
	public void testRerollSkullSorceressFortuneCard() throws Exception {
		DieFace[][] rollSequence = new DieFace[][] {
				{ DieFace.COIN, DieFace.COIN, DieFace.PARROT, DieFace.PARROT, DieFace.PARROT, DieFace.PARROT,
						DieFace.SWORD, DieFace.SWORD },
				{ DieFace.COIN, DieFace.COIN, DieFace.COIN, DieFace.PARROT, DieFace.SKULL, DieFace.PARROT,
						DieFace.SWORD, DieFace.SWORD } };

		FortuneCard fortuneCard = new FortuneCard(FortuneCardType.DIAMOND);
		Turn turn = new Turn(fortuneCard, rollSequence);

		turn.getDice().getAll().get(0).setHeld(true);
		turn.getDice().getAll().get(1).setHeld(true);

		// First roll is done automatically
		turn.rollDice();

		ScoreEvaluator evaluator = new ScoreEvaluator(turn.getDice(), fortuneCard, turn.isDisqualified());
		assertEquals(Integer.valueOf(500), evaluator.evaluate());
	}

	@Test
	public void testTwoMonkeysOneParrotTwoCoinsOneDiamondTwoSwordsSeveralRollsMonkeyBusinessFortuneCard()
			throws Exception {
		DieFace[][] rollSequence = new DieFace[][] {
				{ DieFace.MONKEY, DieFace.MONKEY, DieFace.PARROT, DieFace.PARROT, DieFace.PARROT, DieFace.PARROT,
						DieFace.SWORD, DieFace.SWORD },
				{ DieFace.MONKEY, DieFace.MONKEY, DieFace.PARROT, DieFace.COIN, DieFace.PARROT, DieFace.PARROT,
						DieFace.MONKEY, DieFace.SWORD },
				{ DieFace.MONKEY, DieFace.MONKEY, DieFace.PARROT, DieFace.COIN, DieFace.COIN, DieFace.DIAMOND,
						DieFace.SWORD, DieFace.SWORD } };

		FortuneCard fortuneCard = new FortuneCard(FortuneCardType.MONKEY_BUSINESS);
		Turn turn = new Turn(fortuneCard, rollSequence);

		turn.getDice().getAll().get(0).setHeld(true);
		turn.getDice().getAll().get(1).setHeld(true);

		// First roll is done automatically
		turn.rollDice();

		turn.getDice().getAll().get(2).setHeld(true);
		turn.getDice().getAll().get(3).setHeld(true);
		turn.getDice().getAll().get(7).setHeld(true);

		turn.rollDice();

		ScoreEvaluator evaluator = new ScoreEvaluator(turn.getDice(), fortuneCard, turn.isDisqualified());
		assertEquals(Integer.valueOf(400), evaluator.evaluate());
	}

	@Test
	public void testThreeMonkeysFourParrotsOneSwordSeveralRollsMonkeyBusinessFortuneCard() throws Exception {
		DieFace[][] rollSequence = new DieFace[][] {
				{ DieFace.MONKEY, DieFace.MONKEY, DieFace.PARROT, DieFace.PARROT, DieFace.COIN, DieFace.PARROT,
						DieFace.SWORD, DieFace.SWORD },
				{ DieFace.MONKEY, DieFace.MONKEY, DieFace.PARROT, DieFace.SWORD, DieFace.PARROT, DieFace.PARROT,
						DieFace.MONKEY, DieFace.SWORD },
				{ DieFace.MONKEY, DieFace.MONKEY, DieFace.MONKEY, DieFace.PARROT, DieFace.PARROT, DieFace.PARROT,
						DieFace.PARROT, DieFace.SWORD } };

		FortuneCard fortuneCard = new FortuneCard(FortuneCardType.MONKEY_BUSINESS);
		Turn turn = new Turn(fortuneCard, rollSequence);

		turn.getDice().getAll().get(0).setHeld(true);
		turn.getDice().getAll().get(1).setHeld(true);

		// First roll is done automatically
		turn.rollDice();

		turn.getDice().getAll().get(4).setHeld(true);

		turn.rollDice();

		ScoreEvaluator evaluator = new ScoreEvaluator(turn.getDice(), fortuneCard, turn.isDisqualified());
		assertEquals(Integer.valueOf(2000), evaluator.evaluate());
	}

	@Test
	public void testTreasureChestScenarioA() throws Exception {
		DieFace[][] rollSequence = new DieFace[][] {
				{ DieFace.PARROT, DieFace.PARROT, DieFace.PARROT, DieFace.SWORD, DieFace.SWORD, DieFace.DIAMOND,
						DieFace.DIAMOND, DieFace.COIN },
				{ DieFace.PARROT, DieFace.PARROT, DieFace.PARROT, DieFace.PARROT, DieFace.PARROT, DieFace.DIAMOND,
						DieFace.DIAMOND, DieFace.COIN },
				{ DieFace.PARROT, DieFace.PARROT, DieFace.PARROT, DieFace.PARROT, DieFace.PARROT, DieFace.SKULL,
						DieFace.COIN, DieFace.PARROT } };

		FortuneCard fortuneCard = new FortuneCard(FortuneCardType.TREASURE_CHEST);
		Turn turn = new Turn(fortuneCard, rollSequence);

		turn.getDice().getAll().get(0).setHeld(true);
		turn.getDice().getAll().get(1).setHeld(true);
		turn.getDice().getAll().get(2).setHeld(true);

		turn.getDice().getAll().get(5).setInTreasureChest(true);
		turn.getDice().getAll().get(6).setInTreasureChest(true);
		turn.getDice().getAll().get(7).setInTreasureChest(true);

		// First roll is done automatically
		turn.rollDice();

		turn.getDice().getAll().get(5).setInTreasureChest(false);
		turn.getDice().getAll().get(6).setInTreasureChest(false);
		turn.getDice().getAll().get(7).setInTreasureChest(false);

		turn.getDice().getAll().get(0).setInTreasureChest(true);
		turn.getDice().getAll().get(1).setInTreasureChest(true);
		turn.getDice().getAll().get(2).setInTreasureChest(true);
		turn.getDice().getAll().get(3).setInTreasureChest(true);
		turn.getDice().getAll().get(4).setInTreasureChest(true);

		turn.rollDice();

		turn.getDice().getAll().get(6).setInTreasureChest(true);
		turn.getDice().getAll().get(7).setInTreasureChest(true);

		assertFalse(turn.isDisqualified());

		ScoreEvaluator evaluator = new ScoreEvaluator(turn.getDice(), fortuneCard, turn.isDisqualified());
		assertEquals(Integer.valueOf(1100), evaluator.evaluate());
	}

	@Test
	public void testTreasureChestScenarioB() throws Exception {
		DieFace[][] rollSequence = new DieFace[][] {
				{ DieFace.SKULL, DieFace.SKULL, DieFace.PARROT, DieFace.PARROT, DieFace.PARROT, DieFace.COIN,
						DieFace.COIN, DieFace.COIN },
				{ DieFace.SKULL, DieFace.SKULL, DieFace.DIAMOND, DieFace.DIAMOND, DieFace.COIN, DieFace.COIN,
						DieFace.COIN, DieFace.COIN },
				{ DieFace.SKULL, DieFace.SKULL, DieFace.SKULL, DieFace.COIN, DieFace.COIN, DieFace.COIN, DieFace.COIN,
						DieFace.COIN } };

		FortuneCard fortuneCard = new FortuneCard(FortuneCardType.TREASURE_CHEST);
		Turn turn = new Turn(fortuneCard, rollSequence);

		turn.getDice().getAll().get(5).setInTreasureChest(true);
		turn.getDice().getAll().get(6).setInTreasureChest(true);
		turn.getDice().getAll().get(7).setInTreasureChest(true);

		// First roll is done automatically
		turn.rollDice();

		turn.getDice().getAll().get(4).setInTreasureChest(true);

		turn.rollDice();

		assertTrue(turn.isDisqualified());

		ScoreEvaluator evaluator = new ScoreEvaluator(turn.getDice(), fortuneCard, turn.isDisqualified());
		assertEquals(Integer.valueOf(600), evaluator.evaluate());
	}
}
