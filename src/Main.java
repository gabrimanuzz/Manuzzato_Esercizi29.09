import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        System.out.println("\n=== 1. SIMULAZIONE RICEZIONE PACCHETTI TELEMETRIA ===");

        String[] pacchettiGrezz = {
                "temp=23.5;umid=61;ts=1732000000;batt_low=false",  // 1. Valido completo
                "temp=18.2;umid=45;ts=1732000060;batt_low=true",   // 2. Valido completo
                "temp=xx.xx;umid=70;ts=1732000120;batt_low=false", // 3. Temp corrotta (deve restare null)
                "temp=21.0;umid=150;ts=1732000180",                // 4. Umidità fuori range (eccezione)
                "umid=80;ts=1732000240;batt_low=false",             // 5. Manca temperatura (temp=null)
                "temp=0.0;umid=55;ts=1732000300;batt_low=false",   // 6. Temp = 0.0 (valore reale, non null!)
                "",                                                // 7. Pacchetto vuoto
                "temp=26.8;umid=30;ts=INVALID;batt_low=false"      // 8. Timestamp corrotto
        };

        List<LetturaSensore> lettureValide = new ArrayList<>();
        int erroriParsingGenerali = 0;

        for (int i = 0; i < pacchettiGrezz.length; i++) {
            System.out.println("\n--- Parsing Pacchetto #" + (i + 1) + " ---");
            try {
                var esito = LetturaSensore.parsePacchetto(pacchettiGrezz[i]);
                if (esito.isPresent()) {
                    lettureValide.add(esito.get());
                    System.out.println("Risultato: " + esito.get());
                } else {
                    erroriParsingGenerali++;
                }
            } catch (InvalidInputException e) {
                System.err.println("ERRORE DOMINIO: " + e.getMessage());
                erroriParsingGenerali++;
            }
        }

        System.out.println("\n=== REPORT FINALE ===");
        System.out.println("Numero letture valide elaborate: " + lettureValide.size());
        System.out.println("Numero errori/pacchetti scartati: " + erroriParsingGenerali);

        // Calcolo della media delle temperature (facendo attenzione a null!)
        double sommaTemp = 0.0;
        int conteggioTemp = 0;

        for (LetturaSensore l : lettureValide) {
            if (l.getTemperatura() != null) {
                sommaTemp += l.getTemperatura(); // Autounboxing sicuro
                conteggioTemp++;
            }
        }

        if (conteggioTemp > 0) {
            double media = sommaTemp / conteggioTemp;
            System.out.printf("Media temperature valide (%d misurazioni): %.2f °C\n", conteggioTemp, media);
        } else {
            System.out.println("Nessuna misurazione di temperatura valida disponibile.");
        }
    }
}