package interpreter;

import java.io.*;
import java.net.URL;
import java.net.MalformedURLException;
import storage.TupleSB;

public class Verbo implements Serializable {
  private transient URL link = null;
  private TupleSB verbo = null;
  private TupleSB gerundio = null;
  private TupleSB participioPassado = null;
  private TupleSB[][] indicativo = null;
  private TupleSB[][] conjuntivo = null;
  private TupleSB[][] imperativo = null;
  private Boolean flag = true;

  public Verbo(String verbo){
    BufferedReader reader;
    try{
      link = new URL("http://www.conjuga-me.net/verbo-" + verbo);

      reader = new BufferedReader(new InputStreamReader(link.openStream(), "ISO-8859-1"));

      this.checkIfValid(reader);

      this.verbo = new TupleSB();
      this.verbo.addConju(verbo, true);
      TupleSB[] extract = this.findGerPar(reader);
      this.gerundio = extract[0];
      this.participioPassado = extract[1];
      extract = null;

      this.indicativo = this.findIndicativos(reader);
      this.conjuntivo = this.findConjuntivos(reader);
      this.imperativo = this.findImperativos(reader);

    }catch(InvalidVerbException e){
      this.verbo = new TupleSB();
      this.verbo.addConju(verbo, false);
      this.flag = false;
    }catch(MalformedURLException e){

      //TODO:Handle Erros

    }catch(IOException e){

      //TODO:Handle Erros

    }finally{
      reader = null;
    }
  }

  public Boolean isValid(){
    return this.flag;
  }
  public TupleSB getVerbo(){
    return this.verbo;
  }
  public TupleSB getGerundio(){
    return this.gerundio;
  }
  public TupleSB getParticipioPassado(){
    return this.participioPassado;
  }
  public TupleSB[][] getIndicativo(){
    return this.indicativo;
  }
  public TupleSB[][] getConjuntivo(){
    return this.conjuntivo;
  }
  public TupleSB[][] getImperativo(){
    return this.imperativo;
  }

  private void checkIfValid(BufferedReader reading) throws InvalidVerbException{
    String line = null;

    try{
      walkOnFile(reading, "</div><!-- #formverbo -->");
      line = reading.readLine();
      line = reading.readLine();
      line = reading.readLine();
      if (!line.startsWith("<table")) {
        throw new InvalidVerbException();
      }
    }catch(IOException e){

      //TODO:Handle Erros

    }finally{
      line = null;
    }
  }

  private TupleSB[] findGerPar(BufferedReader reading){
    String line;
    TupleSB[] ret = new TupleSB[2];
    try{
      line = walkOnFile(reading, "    <span class=\"gerundio\">Ger");
      ret[0] = new TupleSB();
      ret[0].addConju(line.substring(45,line.length() - 10).replaceAll("\\s+",""), false);

      line = walkOnFile(reading, "    <span class=\"gerundio\">Partic");

      ret[1] = new TupleSB();
      for(String s : line.substring(55, line.length() - 10).replaceAll("\\s+","").split(";")){
        if (s.contains("<span class=\"irreg\">")) {
          ret[1].addConju(s.replaceAll("\\<.*\\>", ""), true);
        }else{
          ret[1].addConju(s, false);
        }
      }

    }catch(IOException e){

      //TODO: Handle Erros

    }finally{
      line = null;
      return ret;
    }
  }

  private TupleSB[][] findIndicativos(BufferedReader reading){
    TupleSB[][] verbos = new TupleSB[6][6];
    String line = null;
    int i, j;
    try{
      for(j = 0; j < 6; j++){
        for (i = 0; i < 3 ; i++) {
          line = walkOnFile(reading,"      <td class=\"output\">");
          if (line.contains("<span class=\"irreg\">")) {
            verbos[i][j] = new TupleSB();
            verbos[i][j].addConju(line.substring(45, line.length() - 12).replaceAll("\\s+",""), true);
          }else{
            verbos[i][j] = new TupleSB();
            verbos[i][j].addConju(line.substring(25, line.length() - 5).replaceAll("\\s+",""), false);
          }
        }
      }

      for(j = 0; j < 6 ; j++){
        for (i = 3; i < 6; i++){
          line = walkOnFile(reading,"      <td class=\"output\">");
          if (line.contains("<span class=\"irreg\">")) {
            verbos[i][j] = new TupleSB();
            verbos[i][j].addConju(line.substring(45, line.length() - 12).replaceAll("\\s+",""), true);
          }else{
            verbos[i][j] = new TupleSB();
            verbos[i][j].addConju(line.substring(25, line.length() - 5).replaceAll("\\s+",""), false);
          }
        }
      }
    }catch(IOException e){

      // TODO: Handle error

    }
    return verbos;
  }

  private TupleSB[][] findConjuntivos(BufferedReader reading){
    TupleSB[][] verbos = new TupleSB[3][6];
    String line = null;
    int i, j;
    try{
      for(j = 0; j < 6; j++) {
        for (i = 0; i < 3 ; i++) {
          line = walkOnFile(reading,"      <td class=\"output\">");
          if (line.contains("<span class=\"irreg\">")) {
            verbos[i][j] = new TupleSB();
            verbos[i][j].addConju(line.substring(45, line.length() - 12).replaceAll("\\s+",""), true);
          }else{
            verbos[i][j] = new TupleSB();
            verbos[i][j].addConju(line.substring(25, line.length() - 5).replaceAll("\\s+",""), false);
          }
        }
      }
    }catch(IOException e){

      // TODO: Handle error

    }
    return verbos;
  }

  private TupleSB[][] findImperativos(BufferedReader reading){
    TupleSB[][] verbos = null;
    String line = null;
    int i, j;
    int variationFix = 0;
    try{
      verbos = new TupleSB[3][6];
      verbos[0][0] = new TupleSB();
      verbos[0][0].addConju("",true);
      verbos[1][0] = new TupleSB();
      verbos[1][0].addConju("",true);
      line = walkOnFile(reading,"      <td colspan=\"2\" class=\"output\" style=\"width: 33%; padding-left: 6%\"><span style=\"color: #2e4fe5;\">para </span> ");
      if (line.contains("<span class=\"irreg\">")) {
        verbos[2][0] = new TupleSB();
        verbos[2][0].addConju(line.substring(138, line.length() - 57).replaceAll("\\s+",""), true);
      }else{
        verbos[2][0] = new TupleSB();
        verbos[2][0].addConju(line.substring(117, line.length() - 45).replaceAll("\\s+",""), false);
      }
      for (i = 1; i < 6; i++) {
        /*Reading Afirmativo*/
        line = walkOnFile(reading,"      <td colspan=\"2\" class=\"output\" style=\"width: 33%; padding-left: 8%\">");
        if (line.contains("<span class=\"irreg\">")) {
          verbos[0][i] = new TupleSB();
          verbos[0][i].addConju(this.findVerb(line, 94).replaceAll("\\s+",""), true);
        }else{
          verbos[0][i] = new TupleSB();
          verbos[0][i].addConju(this.findVerb(line, 74).replaceAll("\\s+",""), false);
        }

        /*Reading Negativo*/
        line = walkOnFile(reading,"      <td colspan=\"2\" class=\"output\" style=\"width: 33%; padding-left: 8%\"><span style=\"color: #2e4fe5;\">não ");
        if (line.contains("</span><span class=\"irreg\">")) {
          verbos[1][i] = new TupleSB();
          verbos[1][i].addConju(this.findVerb(line, 135).replaceAll("\\s+",""), true);
        }else{
          verbos[1][i] = new TupleSB();
          verbos[1][i].addConju(this.findVerb(line, 115).replaceAll("\\s+",""), false);
        }

        /*Reading Infinitivo Pessoal*/
        line = walkOnFile(reading,"      <td colspan=\"2\" class=\"output\" style=\"width: 33%; padding-left: 6%\"><span style=\"color: #2e4fe5;\">para </span> ");
        if (line.contains("</span><span class=\"irreg\">")) {
          verbos[2][i] = new TupleSB();
          verbos[2][i].addConju(this.findVerb(line, 138).replaceAll("\\s+",""), true);
        }else{
          verbos[2][i] = new TupleSB();
          verbos[2][i].addConju(this.findVerb(line, 117).replaceAll("\\s+",""), false);
        }
      }
    }catch(IOException e){
      verbos = null;
      //TODO: Handle Erros

    }finally{
      line = null;
    }
    return verbos;
  }

  private String findVerb(String line, int baseSize){
    String s = line.substring(baseSize);
    int i = 0;
    char c;
    do{
      i++;
      c = s.charAt(i);
    }while(c != '<');
    return s.substring(0,i);
  }

  private String walkOnFile(BufferedReader reading, String term) throws IOException {
    String line;
    do{
      line = reading.readLine();
    }while(!line.startsWith(term));
    return line;
  }
}
