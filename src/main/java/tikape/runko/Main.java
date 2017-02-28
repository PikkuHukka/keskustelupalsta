package tikape.runko;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.AlueDao;
import tikape.runko.database.AvausDao;
import tikape.runko.database.Database;
import tikape.runko.database.OpiskelijaDao;
import tikape.runko.database.VastausDao;
import tikape.runko.domain.Alue;
import tikape.runko.domain.Avaus;
import tikape.runko.domain.Vastaus;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:keskustelupalsta.db");
        database.init();

        OpiskelijaDao opiskelijaDao = new OpiskelijaDao(database);
        VastausDao vastausDao = new VastausDao(database);
        AlueDao alueDao = new AlueDao(database);
        AvausDao avausDao = new AvausDao(database);
        Random random = new Random();

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();

            List<Alue> alueet = alueDao.findAll();

            for (Alue alue : alueet) {
                alue.setLukumaara(alueDao.viestienLukumaara(alue.getAlue_id()) + alueDao.onkoAvausta(alue.getAlue_id()));
                alue.setViimeisin(alueDao.viimeisinViesti(alue.getAlue_id()));
            }

            map.put("alueet", alueet);

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        post("/", (req, res) -> {
            String nimi = req.queryParams("nimi");
            Alue a = new Alue(Math.abs(nimi.hashCode() + random.nextInt(100)), nimi);
            
            if (!a.getNimi().equals("")) {
                alueDao.lisaaAlue(a);
            }
            
            res.redirect("/");
            return "";
        });
        //Avaus
        
        //req.pathinfo() kertoo polun
        get("/:alue/", (req, res) -> {

            HashMap map = new HashMap<>();
//          map.put("avaus", avausDao.findOne(Integer.parseInt(req.params("id")))));
            map.put("avaukset", avausDao.findAlue(Integer.parseInt(req.params("alue"))));
            map.put("alue", alueDao.findOne(Integer.parseInt(req.params("alue"))));
            return new ModelAndView(map, "avaus");

        }, new ThymeleafTemplateEngine());
        // public Avaus(int avaus_id, int alueviittaus, String otsikko, String sisalto, String nimimerkki, String aikaleima) 
        post("/:alue/", (Request req, Response res) -> {

            String otsikko = req.queryParams("otsikko");
            String sisalto = req.queryParams("sisalto");
            String nimimerkki = req.queryParams("nimimerkki");
            Avaus a = new Avaus(Math.abs(otsikko.hashCode() + random.nextInt(100)), 5, otsikko, sisalto, nimimerkki, new Timestamp(System.currentTimeMillis()));
            avausDao.lisaaAvaus(a);
            res.redirect("/:alue/");
            return "";
        });
        
        
        
        
        //Vastaus
        get("/:alue/:avaus/", (req, res) -> {
            
            HashMap map = new HashMap<>();
            map.put("avaus", avausDao.findOne(Integer.parseInt(req.params("avaus"))));            
            map.put("vastaukset", vastausDao.findAvaus(Integer.parseInt(req.params("avaus"))));            
            map.put("alue", alueDao.findOne(Integer.parseInt(req.params("alue"))));            
            return new ModelAndView(map, "keskustelu");

        }, new ThymeleafTemplateEngine());
        
//            public Vastaus(int vastaus_id, int avausviittaus, int alueviittaus,
//           String sisalto, String nimimerkki, Timestamp aikaleima) {

        post("/:vastaa", (Request req, Response res) -> {
            String sisalto = req.queryParams("vastaus");
            System.out.println(sisalto);
            Integer vastaus_id = sisalto.hashCode() + random.nextInt(100);
            System.out.println(vastaus_id);
            Integer avausviittaus = Integer.parseInt(req.queryParams("avaus"));
            System.out.println(avausviittaus);
            Integer alueviittaus = Integer.parseInt(req.queryParams("alue"));
            System.out.println(alueviittaus.toString());
            String nimimerkki = req.queryParams("nimimerkki");
            System.out.println(nimimerkki);
            Timestamp aikaleima = new java.sql.Timestamp(System.currentTimeMillis());
            System.out.println(aikaleima);
            
            vastausDao.add
            (vastaus_id, sisalto, nimimerkki, aikaleima, avausviittaus, alueviittaus);
            
            res.redirect("/" + alueviittaus + "/" + avausviittaus +"/");
            return "";
        });


    }
}
