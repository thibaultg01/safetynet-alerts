package com.safetynet.alerts.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Classe responsable du chargement initial des données à partir du fichier JSON
 * data.json situé dans le classpath. Cette classe est utilisée comme composant
 * Spring unique via l'annotation Component.
 * 
 * Elle utilise ObjectMapper pour parser les données et les répartir en trois
 * listes : Person, Firestation et MedicalRecord}.
 *
 * Le chargement se fait automatiquement au démarrage de l'application grâce à
 * la méthode annotée avec PostConstruct.
 */
@Component
public class DataLoader {

	private ObjectMapper objectMapper = new ObjectMapper();

	private List<Person> persons = new ArrayList<>();
	private List<Firestation> firestations = new ArrayList<>();
	private List<MedicalRecord> medicalRecords = new ArrayList<>();

	/**
	 * Constructeur par défaut requis pour Spring
	 */
	public DataLoader() {
		// jsonFilePath reste null, Spring utilisera init()
	}

	/**
	 * Méthode appelée automatiquement par Spring après l'instanciation du bean.
	 * Elle charge et parse le fichier data.json depuis le classpath. En cas
	 * d'erreur, une RuntimeException est levée.
	 */
	@PostConstruct
	public void init() {
		try (InputStream inputStream = new ClassPathResource("data.json").getInputStream()) {
			Map<String, Object> fullData = objectMapper.readValue(inputStream, new TypeReference<>() {
			});
			loadListsFromMap(fullData);
		} catch (Exception e) {
			throw new RuntimeException("Erreur lors du chargement de data.json depuis le classpath", e);
		}
	}

	/**
	 * Convertit les données extraites du fichier JSON en listes typées et les
	 * stocke dans les attributs #persons}, #firestations}, et #medicalRecords}.
	 *
	 * @param fullData Une map contenant les clés "persons", "firestations" et
	 *                 "medicalRecords" associées à leurs données respectives.
	 */
	private void loadListsFromMap(Map<String, Object> fullData) {
		this.persons = objectMapper.convertValue(fullData.getOrDefault("persons", new ArrayList<>()),
				new TypeReference<>() {
				});
		this.firestations = objectMapper.convertValue(fullData.getOrDefault("firestations", new ArrayList<>()),
				new TypeReference<>() {
				});
		this.medicalRecords = objectMapper.convertValue(fullData.getOrDefault("medicalRecords", new ArrayList<>()),
				new TypeReference<>() {
				});
	}

	/**
	 * @return la liste des personnes chargées depuis le fichier JSON.
	 */
	public List<Person> getPersons() {
		return persons;
	}

	/**
	 * @return la liste des mappings caserne/adresse chargés depuis le fichier JSON.
	 */
	public List<Firestation> getFirestations() {
		return firestations;
	}

	/**
	 * @return la liste des dossiers médicaux chargés depuis le fichier JSON.
	 */
	public List<MedicalRecord> getMedicalRecords() {
		return medicalRecords;
	}

}