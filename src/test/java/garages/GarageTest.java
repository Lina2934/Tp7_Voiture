package garages;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GarageTest {

	private Garage garageCastres, garageAlbi;
	private Voiture voitureISIS;

	@BeforeEach
	public void setUp() {
		voitureISIS = new Voiture("123 XX 456");
		garageCastres = new Garage("ISIS Castres");
		garageAlbi = new Garage("Universite Champollion Albi");
	}

	@Test
	void lesVoituresSontBienInitialisees() {
		assertFalse(voitureISIS.estDansUnGarage(), "La voiture ne doit pas être dans un garage après initialisation");
		assertTrue(voitureISIS.garagesVisites().isEmpty(), "La voiture ne doit avoir visité aucun garage après initialisation");
	}

	@Test
	void entrerAuGarageChangeGarageVisites() throws Exception {
		voitureISIS.entreAuGarage(garageCastres);
		assertTrue(voitureISIS.estDansUnGarage(), "La voiture doit être dans un garage après y être entrée");
		assertTrue(voitureISIS.garagesVisites().contains(garageCastres), "Le garage visité doit être dans la liste des garages visités");
	}

	@Test
	void sortirDuGarageChangeGarageVisites() throws Exception {
		voitureISIS.entreAuGarage(garageCastres);
		voitureISIS.sortDuGarage();
		assertFalse(voitureISIS.estDansUnGarage(), "La voiture ne doit plus être dans un garage après en être sortie");
		assertTrue(voitureISIS.garagesVisites().contains(garageCastres), "Le garage visité doit toujours être dans la liste des garages visités");
	}

	@Test
	void pasDeDoubleSortie() {
		voitureISIS.entreAuGarage(garageCastres);
		voitureISIS.sortDuGarage();
		try {
			voitureISIS.sortDuGarage();
			fail("Une exception devait être levée en cas de double sortie");
		} catch (IllegalStateException e) {
			// Exception attendue
		}
	}

	@Test
	void pasDeDoubleEntree() {
		voitureISIS.entreAuGarage(garageCastres);
		try {
			voitureISIS.entreAuGarage(garageAlbi);
			fail("Une exception devait être levée en cas de double entrée");
		} catch (IllegalStateException e) {
			// Exception attendue
		}
	}

	@Test
	void testCorrectPrintFormat() throws Exception {
		voitureISIS.entreAuGarage(garageCastres);
		voitureISIS.sortDuGarage();
		voitureISIS.entreAuGarage(garageAlbi);
		voitureISIS.sortDuGarage();
		voitureISIS.entreAuGarage(garageCastres);

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(os);
		voitureISIS.imprimeStationnements(ps);

		String output = os.toString("UTF8");

		assertEquals(1, countSubstring(output, garageCastres.toString()),
				garageCastres.toString() + " doit apparaître une fois");
		assertEquals(1, countSubstring(output, garageAlbi.toString()),
				garageAlbi.toString() + " doit apparaître une fois");
		assertEquals(3, countSubstring(output, "Stationnement"),
				"On doit imprimer trois stationnements");
		assertEquals(1, countSubstring(output, "en cours"),
				"Il doit y avoir un seul stationnement en cours");
	}

	private int countSubstring(final String string, final String substring) {
		int count = 0;
		int idx = 0;
		while ((idx = string.indexOf(substring, idx)) != -1) {
			idx++;
			count++;
		}
		return count;
	}
}
