package com.example.pharmacymanagementsystem.initializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.pharmacymanagementsystem.model.Location;
import com.example.pharmacymanagementsystem.repository.LocationRepository;

@Component
@Order(1)
public class LocationDataInitializer implements CommandLineRunner {

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public void run(String... args) throws Exception {
        try {
            if (locationRepository.count() == 0) {
                System.out.println("Initializing Rwanda location data...");
                initializeRwandaLocations();
                System.out.println("Location data initialization completed successfully!");
            } else {
                System.out.println("Location data already exists, skipping initialization.");
            }
        } catch (Exception e) {
            System.err.println("Error initializing location data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initializeRwandaLocations() {
        // Provinces
        Location kigali = createLocation("Kigali City", "KGL", Location.LocationType.PROVINCE, null);
        Location southern = createLocation("Southern Province", "SP", Location.LocationType.PROVINCE, null);
        Location western = createLocation("Western Province", "WP", Location.LocationType.PROVINCE, null);
        Location northern = createLocation("Northern Province", "NP", Location.LocationType.PROVINCE, null);
        Location eastern = createLocation("Eastern Province", "EP", Location.LocationType.PROVINCE, null);

        // All Districts
        Location gasabo = createLocation("Gasabo", "GAS", Location.LocationType.DISTRICT, kigali);
        Location kicukiro = createLocation("Kicukiro", "KIC", Location.LocationType.DISTRICT, kigali);
        Location nyarugenge = createLocation("Nyarugenge", "NYA", Location.LocationType.DISTRICT, kigali);
        Location huye = createLocation("Huye", "HUY", Location.LocationType.DISTRICT, southern);
        Location muhanga = createLocation("Muhanga", "MUH", Location.LocationType.DISTRICT, southern);
        Location nyanza = createLocation("Nyanza", "NYZ", Location.LocationType.DISTRICT, southern);
        Location ruhango = createLocation("Ruhango", "RUH", Location.LocationType.DISTRICT, southern);
        Location nyaruguru = createLocation("Nyaruguru", "NYR", Location.LocationType.DISTRICT, southern);
        Location nyamagabe = createLocation("Nyamagabe", "NYM", Location.LocationType.DISTRICT, southern);
        Location gisagara = createLocation("Gisagara", "GIS", Location.LocationType.DISTRICT, southern);
        Location kamonyi = createLocation("Kamonyi", "KAM", Location.LocationType.DISTRICT, southern);
        Location karongi = createLocation("Karongi", "KAR", Location.LocationType.DISTRICT, western);
        Location rutsiro = createLocation("Rutsiro", "RUT", Location.LocationType.DISTRICT, western);
        Location rubavu = createLocation("Rubavu", "RUB", Location.LocationType.DISTRICT, western);
        Location nyabihu = createLocation("Nyabihu", "NYB", Location.LocationType.DISTRICT, western);
        Location ngororero = createLocation("Ngororero", "NGO", Location.LocationType.DISTRICT, western);
        Location rusizi = createLocation("Rusizi", "RUS", Location.LocationType.DISTRICT, western);
        Location nyamasheke = createLocation("Nyamasheke", "NYS", Location.LocationType.DISTRICT, western);
        Location rulindo = createLocation("Rulindo", "RUL", Location.LocationType.DISTRICT, northern);
        Location gakenke = createLocation("Gakenke", "GAK", Location.LocationType.DISTRICT, northern);
        Location musanze = createLocation("Musanze", "MUS", Location.LocationType.DISTRICT, northern);
        Location burera = createLocation("Burera", "BUR", Location.LocationType.DISTRICT, northern);
        Location gicumbi = createLocation("Gicumbi", "GIC", Location.LocationType.DISTRICT, northern);
        Location rwamagana = createLocation("Rwamagana", "RWA", Location.LocationType.DISTRICT, eastern);
        Location nyagatare = createLocation("Nyagatare", "NYG", Location.LocationType.DISTRICT, eastern);
        Location gatsibo = createLocation("Gatsibo", "GAT", Location.LocationType.DISTRICT, eastern);
        Location kayonza = createLocation("Kayonza", "KAY", Location.LocationType.DISTRICT, eastern);
        Location kirehe = createLocation("Kirehe", "KIR", Location.LocationType.DISTRICT, eastern);
        Location ngoma = createLocation("Ngoma", "NGM", Location.LocationType.DISTRICT, eastern);
        Location bugesera = createLocation("Bugesera", "BUG", Location.LocationType.DISTRICT, eastern);

        // Gasabo Sectors
        Location kacyiru = createLocation("Kacyiru", "KCY", Location.LocationType.SECTOR, gasabo);
        Location kimihurura = createLocation("Kimihurura", "KMH", Location.LocationType.SECTOR, gasabo);
        Location remera = createLocation("Remera", "RMR", Location.LocationType.SECTOR, gasabo);
        Location kinyinya = createLocation("Kinyinya", "KNY", Location.LocationType.SECTOR, gasabo);
        Location bumbogo = createLocation("Bumbogo", "BMB", Location.LocationType.SECTOR, gasabo);
        Location gatsata = createLocation("Gatsata", "GTS", Location.LocationType.SECTOR, gasabo);
        Location gikomero = createLocation("Gikomero", "GKM", Location.LocationType.SECTOR, gasabo);
        Location gisozi = createLocation("Gisozi", "GSZ", Location.LocationType.SECTOR, gasabo);
        Location jabana = createLocation("Jabana", "JBN", Location.LocationType.SECTOR, gasabo);
        Location jali = createLocation("Jali", "JLI", Location.LocationType.SECTOR, gasabo);
        Location kageyo = createLocation("Kageyo", "KGY", Location.LocationType.SECTOR, gasabo);
        Location kimironko = createLocation("Kimironko", "KMR", Location.LocationType.SECTOR, gasabo);
        Location nduba = createLocation("Nduba", "NDB", Location.LocationType.SECTOR, gasabo);
        Location ndera = createLocation("Ndera", "NDR", Location.LocationType.SECTOR, gasabo);
        Location rusororo = createLocation("Rusororo", "RSR", Location.LocationType.SECTOR, gasabo);

        // Kicukiro Sectors
        Location gatenga = createLocation("Gatenga", "GTG", Location.LocationType.SECTOR, kicukiro);
        Location niboye = createLocation("Niboye", "NBY", Location.LocationType.SECTOR, kicukiro);
        Location kagarama = createLocation("Kagarama", "KGR", Location.LocationType.SECTOR, kicukiro);
        Location kanombe = createLocation("Kanombe", "KNB", Location.LocationType.SECTOR, kicukiro);
        Location kicukiro_sector = createLocation("Kicukiro", "KCK", Location.LocationType.SECTOR, kicukiro);
        Location masaka = createLocation("Masaka", "MSK", Location.LocationType.SECTOR, kicukiro);
        Location nyarugunga = createLocation("Nyarugunga", "NYG", Location.LocationType.SECTOR, kicukiro);
        Location ruhango_kicukiro = createLocation("Ruhango", "RHG", Location.LocationType.SECTOR, kicukiro);
        Location rwampara = createLocation("Rwampara", "RWP", Location.LocationType.SECTOR, kicukiro);
        Location zana = createLocation("Zana", "ZNA", Location.LocationType.SECTOR, kicukiro);

        // Nyarugenge Sectors
        Location gitega = createLocation("Gitega", "GTG", Location.LocationType.SECTOR, nyarugenge);
        Location kanyinya = createLocation("Kanyinya", "KNY", Location.LocationType.SECTOR, nyarugenge);
        Location kigali_sector = createLocation("Kigali", "KGL", Location.LocationType.SECTOR, nyarugenge);
        Location kimisagara = createLocation("Kimisagara", "KMS", Location.LocationType.SECTOR, nyarugenge);
        Location mageragere = createLocation("Mageragere", "MGR", Location.LocationType.SECTOR, nyarugenge);
        Location muhima = createLocation("Muhima", "MHM", Location.LocationType.SECTOR, nyarugenge);
        Location nyakabanda = createLocation("Nyakabanda", "NKB", Location.LocationType.SECTOR, nyarugenge);
        Location nyamirambo = createLocation("Nyamirambo", "NYM", Location.LocationType.SECTOR, nyarugenge);
        Location rwezamenyo = createLocation("Rwezamenyo", "RWZ", Location.LocationType.SECTOR, nyarugenge);

        // Huye Sectors
        Location tumba = createLocation("Tumba", "TMB", Location.LocationType.SECTOR, huye);
        Location ngoma_huye = createLocation("Ngoma", "NGM", Location.LocationType.SECTOR, huye);
        Location mukura = createLocation("Mukura", "MKR", Location.LocationType.SECTOR, huye);
        Location gishamvu = createLocation("Gishamvu", "GSH", Location.LocationType.SECTOR, huye);
        Location karama = createLocation("Karama", "KRM", Location.LocationType.SECTOR, huye);
        Location kigoma = createLocation("Kigoma", "KGM", Location.LocationType.SECTOR, huye);
        Location kinazi = createLocation("Kinazi", "KNZ", Location.LocationType.SECTOR, huye);
        Location maraba = createLocation("Maraba", "MRB", Location.LocationType.SECTOR, huye);
        Location mbazi = createLocation("Mbazi", "MBZ", Location.LocationType.SECTOR, huye);
        Location mukamira = createLocation("Mukamira", "MKM", Location.LocationType.SECTOR, huye);
        Location rusatira = createLocation("Rusatira", "RST", Location.LocationType.SECTOR, huye);
        Location rwaniro = createLocation("Rwaniro", "RWN", Location.LocationType.SECTOR, huye);
        Location simbi = createLocation("Simbi", "SMB", Location.LocationType.SECTOR, huye);
        Location ruhashya = createLocation("Ruhashya", "RHS", Location.LocationType.SECTOR, huye);

        // Musanze Sectors
        Location muhoza = createLocation("Muhoza", "MHZ", Location.LocationType.SECTOR, musanze);
        Location cyuve_sector = createLocation("Cyuve", "CYV", Location.LocationType.SECTOR, musanze);
        Location busogo = createLocation("Busogo", "BSG", Location.LocationType.SECTOR, musanze);
        Location gacaca = createLocation("Gacaca", "GCC", Location.LocationType.SECTOR, musanze);
        Location gashaki = createLocation("Gashaki", "GSK", Location.LocationType.SECTOR, musanze);
        Location gataraga = createLocation("Gataraga", "GTR", Location.LocationType.SECTOR, musanze);
        Location kimonyi = createLocation("Kimonyi", "KMN", Location.LocationType.SECTOR, musanze);
        Location kinigi = createLocation("Kinigi", "KNG", Location.LocationType.SECTOR, musanze);
        Location muko = createLocation("Muko", "MKO", Location.LocationType.SECTOR, musanze);
        Location musanze_sector = createLocation("Musanze", "MSZ", Location.LocationType.SECTOR, musanze);
        Location nkotsi = createLocation("Nkotsi", "NKT", Location.LocationType.SECTOR, musanze);
        Location nyange = createLocation("Nyange", "NYG", Location.LocationType.SECTOR, musanze);
        Location remera_musanze = createLocation("Remera", "RMR", Location.LocationType.SECTOR, musanze);
        Location shingiro = createLocation("Shingiro", "SHG", Location.LocationType.SECTOR, musanze);

        // Cells for major sectors
        Location kamatamu = createLocation("Kamatamu", "KMT", Location.LocationType.CELL, kacyiru);
        Location kibagabaga = createLocation("Kibagabaga", "KBG", Location.LocationType.CELL, kacyiru);
        Location kacyiru_cell = createLocation("Kacyiru Cell", "KCY", Location.LocationType.CELL, kacyiru);
        Location ubumwe_kacyiru = createLocation("Ubumwe", "UBW", Location.LocationType.CELL, kacyiru);
        Location urumuri_kacyiru = createLocation("Urumuri", "URM", Location.LocationType.CELL, kacyiru);

        Location kimihurura_cell = createLocation("Kimihurura Cell", "KMH", Location.LocationType.CELL, kimihurura);
        Location ubusabane_kimihurura = createLocation("Ubusabane", "UBS", Location.LocationType.CELL, kimihurura);
        Location ubwoba_kimihurura = createLocation("Ubwoba", "UBB", Location.LocationType.CELL, kimihurura);
        Location amahoro_kimihurura = createLocation("Amahoro", "AMH", Location.LocationType.CELL, kimihurura);
        Location ubwiyunge_kimihurura = createLocation("Ubwiyunge", "UBY", Location.LocationType.CELL, kimihurura);

        Location remera_cell = createLocation("Remera Cell", "RMR", Location.LocationType.CELL, remera);
        Location kisimenti = createLocation("Kisimenti", "KSM", Location.LocationType.CELL, remera);
        Location rukiri = createLocation("Rukiri", "RKR", Location.LocationType.CELL, remera);
        Location ubumwe_remera = createLocation("Ubumwe Remera", "UBR", Location.LocationType.CELL, remera);
        Location urumuri_remera = createLocation("Urumuri Remera", "URR", Location.LocationType.CELL, remera);

        Location kiyovu = createLocation("Kiyovu", "KYV", Location.LocationType.CELL, kimisagara);
        Location rugenge = createLocation("Rugenge", "RGG", Location.LocationType.CELL, kimisagara);
        Location kimisagara_cell = createLocation("Kimisagara Cell", "KMS", Location.LocationType.CELL, kimisagara);
        Location ubwoba_kimisagara = createLocation("Ubwoba Kimisagara", "UBK", Location.LocationType.CELL, kimisagara);
        Location amahoro_kimisagara = createLocation("Amahoro Kimisagara", "AMK", Location.LocationType.CELL, kimisagara);

        Location gatenga_cell = createLocation("Gatenga Cell", "GTC", Location.LocationType.CELL, gatenga);
        Location kabuye = createLocation("Kabuye", "KBY", Location.LocationType.CELL, gatenga);
        Location kigarama = createLocation("Kigarama", "KGM", Location.LocationType.CELL, gatenga);
        Location ubusabane_gatenga = createLocation("Ubusabane Gatenga", "UBG", Location.LocationType.CELL, gatenga);
        Location ubwoba_gatenga = createLocation("Ubwoba Gatenga", "UBT", Location.LocationType.CELL, gatenga);

        Location matyazo = createLocation("Matyazo", "MTZ", Location.LocationType.CELL, tumba);
        Location tumba_cell = createLocation("Tumba Cell", "TMB", Location.LocationType.CELL, tumba);
        Location cyarwa = createLocation("Cyarwa", "CYW", Location.LocationType.CELL, tumba);
        Location ubwenge_tumba = createLocation("Ubwenge", "UBG", Location.LocationType.CELL, tumba);
        Location ubwoba_tumba = createLocation("Ubwoba Tumba", "UBT", Location.LocationType.CELL, tumba);

        Location cyuve = createLocation("Cyuve", "CYV", Location.LocationType.CELL, muhoza);
        Location muhoza_cell = createLocation("Muhoza Cell", "MHZ", Location.LocationType.CELL, muhoza);
        Location rwaza = createLocation("Rwaza", "RWZ", Location.LocationType.CELL, muhoza);
        Location ubwiyunge_muhoza = createLocation("Ubwiyunge Muhoza", "UBM", Location.LocationType.CELL, muhoza);
        Location amahoro_muhoza = createLocation("Amahoro Muhoza", "AMM", Location.LocationType.CELL, muhoza);

        // Villages
        createLocation("Kamatamu Village", "KMTV", Location.LocationType.VILLAGE, kamatamu);
        createLocation("Ubumwe Village", "UBWV", Location.LocationType.VILLAGE, kamatamu);
        createLocation("Urumuri Village", "URMV", Location.LocationType.VILLAGE, kamatamu);
        createLocation("Amahoro Village", "AMHV", Location.LocationType.VILLAGE, kamatamu);
        createLocation("Ubwiyunge Village", "UBYV", Location.LocationType.VILLAGE, kamatamu);
        createLocation("Ubusabane Village", "UBSV", Location.LocationType.VILLAGE, kamatamu);
        createLocation("Ubwoba Village", "UBBV", Location.LocationType.VILLAGE, kamatamu);
        createLocation("Ubwenge Village", "UBGV", Location.LocationType.VILLAGE, kamatamu);
        createLocation("Ubunyangawe Village", "UBNV", Location.LocationType.VILLAGE, kamatamu);
        createLocation("Ubucuruzi Village", "UBCV", Location.LocationType.VILLAGE, kamatamu);

        createLocation("Kiyovu Village", "KYVV", Location.LocationType.VILLAGE, kiyovu);
        createLocation("Ubwiyunge Kiyovu", "UBYK", Location.LocationType.VILLAGE, kiyovu);
        createLocation("Amahoro Kiyovu", "AMHK", Location.LocationType.VILLAGE, kiyovu);
        createLocation("Ubumwe Kiyovu", "UBWK", Location.LocationType.VILLAGE, kiyovu);
        createLocation("Urumuri Kiyovu", "URMK", Location.LocationType.VILLAGE, kiyovu);
        createLocation("Ubusabane Kiyovu", "UBSK", Location.LocationType.VILLAGE, kiyovu);
        createLocation("Ubwoba Kiyovu", "UBBK", Location.LocationType.VILLAGE, kiyovu);
        createLocation("Ubwenge Kiyovu", "UBGK", Location.LocationType.VILLAGE, kiyovu);
        createLocation("Ubunyangawe Kiyovu", "UBNK", Location.LocationType.VILLAGE, kiyovu);
        createLocation("Ubucuruzi Kiyovu", "UBCK", Location.LocationType.VILLAGE, kiyovu);

        createLocation("Gatenga Village", "GTGV", Location.LocationType.VILLAGE, gatenga_cell);
        createLocation("Ubwoba Gatenga", "UBBG", Location.LocationType.VILLAGE, gatenga_cell);
        createLocation("Ubusabane Gatenga", "UBSG", Location.LocationType.VILLAGE, gatenga_cell);
        createLocation("Amahoro Gatenga", "AMHG", Location.LocationType.VILLAGE, gatenga_cell);
        createLocation("Ubwiyunge Gatenga", "UBYG", Location.LocationType.VILLAGE, gatenga_cell);
        createLocation("Ubumwe Gatenga", "UBWG", Location.LocationType.VILLAGE, gatenga_cell);
        createLocation("Urumuri Gatenga", "URMG", Location.LocationType.VILLAGE, gatenga_cell);
        createLocation("Ubwenge Gatenga", "UBGG", Location.LocationType.VILLAGE, gatenga_cell);
        createLocation("Ubunyangawe Gatenga", "UBNG", Location.LocationType.VILLAGE, gatenga_cell);
        createLocation("Ubucuruzi Gatenga", "UBCG", Location.LocationType.VILLAGE, gatenga_cell);

        createLocation("Matyazo Village", "MTZV", Location.LocationType.VILLAGE, matyazo);
        createLocation("Ubwenge Matyazo", "UBGM", Location.LocationType.VILLAGE, matyazo);
        createLocation("Ubwoba Matyazo", "UBBM", Location.LocationType.VILLAGE, matyazo);
        createLocation("Amahoro Matyazo", "AMHM", Location.LocationType.VILLAGE, matyazo);
        createLocation("Ubwiyunge Matyazo", "UBYM", Location.LocationType.VILLAGE, matyazo);
        createLocation("Ubumwe Matyazo", "UBWM", Location.LocationType.VILLAGE, matyazo);
        createLocation("Urumuri Matyazo", "URMM", Location.LocationType.VILLAGE, matyazo);
        createLocation("Ubusabane Matyazo", "UBSM", Location.LocationType.VILLAGE, matyazo);
        createLocation("Ubunyangawe Matyazo", "UBNM", Location.LocationType.VILLAGE, matyazo);
        createLocation("Ubucuruzi Matyazo", "UBCM", Location.LocationType.VILLAGE, matyazo);

        createLocation("Cyuve Village", "CYVV", Location.LocationType.VILLAGE, cyuve);
        createLocation("Ubwiyunge Cyuve", "UBYC", Location.LocationType.VILLAGE, cyuve);
        createLocation("Amahoro Cyuve", "AMHC", Location.LocationType.VILLAGE, cyuve);
        createLocation("Ubumwe Cyuve", "UBWC", Location.LocationType.VILLAGE, cyuve);
        createLocation("Urumuri Cyuve", "URMC", Location.LocationType.VILLAGE, cyuve);
        createLocation("Ubusabane Cyuve", "UBSC", Location.LocationType.VILLAGE, cyuve);
        createLocation("Ubwoba Cyuve", "UBBC", Location.LocationType.VILLAGE, cyuve);
        createLocation("Ubwenge Cyuve", "UBGC", Location.LocationType.VILLAGE, cyuve);
        createLocation("Ubunyangawe Cyuve", "UBNC", Location.LocationType.VILLAGE, cyuve);
        createLocation("Ubucuruzi Cyuve", "UBCC", Location.LocationType.VILLAGE, cyuve);
    }

    private Location createLocation(String name, String code, Location.LocationType type, Location parent) {
        Location location = Location.builder()
                .name(name)
                .code(code)
                .type(type)
                .parent(parent)
                .build();
        return locationRepository.save(location);
    }
}