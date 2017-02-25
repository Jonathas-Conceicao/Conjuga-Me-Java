package interpreter;

import java.io.IOException;
import java.io.Serializable;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.MalformedURLException;

import storage.Conjugation;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 *  Class responsible to hold the verb conjugations.
 *  Conjugations are stored in a {@link storage.Conjugation}.
 */
public class Verbo implements Serializable {
  private transient URL link = null;
  private Conjugation verbo = null;
  private Conjugation gerundio = null;
  private Conjugation participioPassado = null;
  private Conjugation[][] indicativo = null;
  private Conjugation[][] conjuntivo = null;
  private Conjugation[][] imperativo = null;
  private Boolean flag = true;
  private static final String[] separadores = new String[] {" /", "&hArr;", "&asymp;"};

  /**
   * Class that acess the URL and stores the verb conjucations.
   * @param verbo        Verb to be conjugated
   */
  public Verbo(String verbo){
    BufferedReader reader;
    try{
      link = new URL("http://www.conjuga-me.net/verbo-" + verbo);

      reader = new BufferedReader(new InputStreamReader(link.openStream(), "ISO-8859-1"));

      this.checkIfValid(reader);

      this.verbo = new Conjugation();
      this.verbo.addConju(verbo, true);
      Conjugation[] extract = this.findGerPar(reader);
      this.gerundio = extract[0];
      this.participioPassado = extract[1];
      extract = null;

      this.indicativo = this.findIndicativos(reader);
      this.conjuntivo = this.findConjuntivos(reader);
      this.imperativo = this.findImperativos(reader);

    }catch(InvalidVerbException e){
      this.verbo = new Conjugation();
      this.verbo.addConju(verbo, false);
      this.flag = false;
    }catch(MalformedURLException e){
      e.printStackTrace();
    }catch(IOException e){
      e.printStackTrace();
    }finally{
      reader = null;
    }
  }

  /**
   * Tests if the verb passed is valid
   * @return Returns true if verb  is valid
   */
  public Boolean isValid(){
    return this.flag;
  }

  /**
   * Returns the verb
   * @return Tuple containing the verb
   */
  public Conjugation getVerbo(){
    return this.verbo;
  }

  /**
   * Returns verb's gerundio
   * @return Tuple containing verb's gerundio
   */
  public Conjugation getGerundio(){
    return this.gerundio;
  }

  /**
   * Returns verb's participio passado
   * @return Tuple containing verb's participio passado
   */
  public Conjugation getParticipioPassado(){
    return this.participioPassado;
  }

  /**
   * Returns a list of verb's conjucations on indicativo
   * @return Array of tuples with verb's conjucations
   */
  public Conjugation[][] getIndicativo(){
    return this.indicativo;
  }

 /**
  * Returns a list of verb's conjucations on conjutivo
  * @return Array of tuples with verb's conjucations
  */
  public Conjugation[][] getConjuntivo(){
    return this.conjuntivo;
  }

  /**
   * Returns a list of verb's conjucations on imperativo
   * @return Array of tuples with verb's conjucations
   */
  public Conjugation[][] getImperativo(){
    return this.imperativo;
  }

  /**
   * Checks if the verb is a valid verb
   * @param reading      URL of verb page's
   * @throws InvalidVerbException Exception thrown if verb was not a valid ver
   */
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
      e.printStackTrace();
      //TODO:Handle Erros

    }finally{
      line = null;
    }
  }

  private Conjugation[] findGerPar(BufferedReader reading){
    String line;
    Conjugation[] ret = new Conjugation[2];
    try{
      line = walkOnFile(reading, "    <span class=\"gerundio\">Ger");
      ret[0] = new Conjugation();
      for(String text : this.splitFromSeparadores(this.dropTdClass(line, "</span>(.*)</td>"))){
        if(text.contains("<span class=\"irreg\">")){
          ret[0].addConju(text.replaceAll("<span class=\"irreg\">", "").replaceAll("</span>", "").replaceAll(" ", ""),true);
        }else{
          ret[0].addConju(text.replaceAll(" ", ""),false);
        }
      }

      line = walkOnFile(reading, "    <span class=\"gerundio\">Partic");
      ret[1] = new Conjugation();
      for(String text : this.splitFromSeparadores(this.dropTdClass(line, "</span>(.*)</td>"))){
        if(text.contains("<span class=\"irreg\">")){
          ret[1].addConju(text.replaceAll("<span class=\"irreg\">", "").replaceAll("</span>", "").replaceAll(" ", ""),true);
        }else{
          ret[1].addConju(text.replaceAll(" ", ""),false);
        }
      }

    }catch(IOException e){
      e.printStackTrace();
      //TODO: Handle Erros

    }finally{
      line = null;
      return ret;
    }
  }

  private Conjugation[][] findIndicativos(BufferedReader reading){
    Conjugation[][] verbos = new Conjugation[6][6];
    String line = null;
    int i, j;
    try{
      for(j = 0; j < 6; j++){
        for (i = 0; i < 3 ; i++) {
          line = walkOnFile(reading,"      <td class=\"output\">");
          verbos[i][j] = new Conjugation();
          for(String text : this.splitFromSeparadores(this.dropTdClass(line))){
            if(text.contains("<span class=\"irreg\">")){
              verbos[i][j].addConju(text.replaceAll("<span class=\"irreg\">", "").replaceAll("</span>", "").replaceAll(" ", ""),true);
            }else{
              verbos[i][j].addConju(text.replaceAll(" ", ""),false);
            }
          }
        }
      }

      for(j = 0; j < 6 ; j++){
        for (i = 3; i < 6; i++){
          line = walkOnFile(reading,"      <td class=\"output\">");
          verbos[i][j] = new Conjugation();
          for(String text : this.splitFromSeparadores(this.dropTdClass(line))){
            if(text.contains("<span class=\"irreg\">")){
              verbos[i][j].addConju(text.replaceAll("<span class=\"irreg\">", "").replaceAll("</span>", "").replaceAll(" ", ""),true);
            }else{
              verbos[i][j].addConju(text.replaceAll(" ", ""),false);
            }
          }
        }
      }
    }catch(IOException e){
      e.printStackTrace();
      // TODO: Handle error

    }
    return verbos;
  }

  private Conjugation[][] findConjuntivos(BufferedReader reading){
    Conjugation[][] verbos = new Conjugation[3][6];
    String line = null;
    int i, j;
    try{
      for(j = 0; j < 6; j++) {
        for (i = 0; i < 3 ; i++) {
          line = walkOnFile(reading,"      <td class=\"output\">");
          verbos[i][j] = new Conjugation();
          for(String text : this.splitFromSeparadores(this.dropTdClass(line))){
            if(text.contains("<span class=\"irreg\">")){
              verbos[i][j].addConju(text.replaceAll("<span class=\"irreg\">", "").replaceAll("</span>", "").replaceAll(" ", ""),true);
            }else{
              verbos[i][j].addConju(text.replaceAll(" ", ""),false);
            }
          }
        }
      }
    }catch(IOException e){
      e.printStackTrace();
      // TODO: Handle error

    }
    return verbos;
  }

  private Conjugation[][] findImperativos(BufferedReader reading){
    Conjugation[][] verbos = null;
    String line = null;
    int i, j;
    int variationFix = 0;
    try{
      verbos = new Conjugation[3][6];
      verbos[0][0] = new Conjugation();
      verbos[0][0].addConju("",true);
      verbos[1][0] = new Conjugation();
      verbos[1][0].addConju("",true);
      line = walkOnFile(reading,"      <td colspan=\"2\" class=\"output\" style=\"width: 33%; padding-left: 6%\"><span style=\"color: #2e4fe5;\">para </span> ");
      verbos[2][0] = new Conjugation();
      for(String text : this.splitFromSeparadores(this.dropTdClass(line, "<td.*</span>(.*)<span style="))){
        if(text.contains("<span class=\"irreg\">")){
          verbos[2][0].addConju(text.replaceAll("<span class=\"irreg\">", "").replaceAll("</span>", "").replaceAll(" ", ""),true);
        }else{
          verbos[2][0].addConju(text.replaceAll(" ", ""),false);
        }
      }
      for (i = 1; i < 6; i++) {
        /*Reading Afirmativo*/
        line = walkOnFile(reading,"      <td colspan=\"2\" class=\"output\"");
        verbos[0][i] = new Conjugation();
        for(String text : this.splitFromSeparadores(this.dropTdClass(line, "<td.*%\">(.*)<span style="))){
          if(text.contains("<span class=\"irreg\">")){
            verbos[0][i].addConju(text.replaceAll("<span class=\"irreg\">", "").replaceAll("</span>", "").replaceAll(" ", ""),true);
          }else{
            verbos[0][i].addConju(text.replaceAll(" ", ""),false);
          }
        }
        /*Reading Negativo*/
        line = walkOnFile(reading,"      <td colspan=\"2\" class=\"output\"");
        verbos[1][i] = new Conjugation();
        for(String text : this.splitFromSeparadores(this.dropTdClass(line, "<td.*>.*<span style.*>n.o..?.?</span>(.*)<span style"))){
          if(text.contains("<span class=\"irreg\">")){
            verbos[1][i].addConju(text.replaceAll("<span class=\"irreg\">", "").replaceAll("</span>", "").replaceAll(" ", ""),true);
          }else{
            verbos[1][i].addConju(text.replaceAll(" ", ""),false);
          }
        }

        /*Reading Infinitivo Pessoal*/
        line = walkOnFile(reading,"      <td colspan=\"2\" class=\"output\"");
        verbos[2][i] = new Conjugation();
        for(String text : this.splitFromSeparadores(this.dropTdClass(line, "<td.*>(.*)<span style="))){
          if(text.contains("<span class=\"irreg\">")){
            verbos[2][i].addConju(text.replaceAll("<span class=\"irreg\">", "").replaceAll("</span>", "").replaceAll(" ", ""),true);
          }else{
            verbos[2][i].addConju(text.replaceAll(" ", ""),false);
          }
        }
      }
    }catch(IOException e){
      verbos = null;
      e.printStackTrace();
      //TODO: Handle Erros

    }finally{
      line = null;
    }
    return verbos;
  }

  private String walkOnFile(BufferedReader reading, String term) throws IOException {
    String line;
    do{
      line = reading.readLine();
    }while(!line.startsWith(term));
    return line;
  }

  private String dropTdClass(String s){
    Pattern pattern = Pattern.compile("<td class=\".*?\">(.*?)</td>");
    Matcher matcher = pattern.matcher(s);
    if (matcher.find()){
      return matcher.group(1);
    }
    return "";
  }

  private String dropTdClass(String s, String regex){
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(s);
    if (matcher.find()){
      return matcher.group(1);
    }
    return "";
  }

  private ArrayList<String> splitFromSeparadores(String text){
    ArrayList<String> ret = new ArrayList<String>(2);
    Boolean flag = true;
    for(String s: this.separadores){
      if (text.contains(s)) {
        for(String ss: text.split(s)){
          ret.add(ss);
        }
        flag = false;
      }
    }
    if (flag) {
      ret.add(text);
    }
    return ret;
  }
}
