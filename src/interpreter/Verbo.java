package interpreter;

import java.io.*;

import java.net.URL;
import java.net.MalformedURLException;

import storage.TupleSB;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Verbo implements Serializable {
  private transient URL link = null;
  private TupleSB verbo = null;
  private TupleSB gerundio = null;
  private TupleSB participioPassado = null;
  private TupleSB[][] indicativo = null;
  private TupleSB[][] conjuntivo = null;
  private TupleSB[][] imperativo = null;
  private Boolean flag = true;
  private static final String[] separadores = new String[] {" /", "&hArr;", "&asymp;"};

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
      e.printStackTrace();
      //TODO:Handle Erros

    }catch(IOException e){

      e.printStackTrace();
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
      e.printStackTrace();
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
      for(String text : this.splitFromSeparadores(this.dropTdClass(line, "</span>(.*)</td>"))){
        if(text.contains("<span class=\"irreg\">")){
          ret[0].addConju(text.replaceAll("<span class=\"irreg\">", "").replaceAll("</span>", "").replaceAll(" ", ""),true);
        }else{
          ret[0].addConju(text.replaceAll(" ", ""),false);
        }
      }

      line = walkOnFile(reading, "    <span class=\"gerundio\">Partic");
      ret[1] = new TupleSB();
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

  private TupleSB[][] findIndicativos(BufferedReader reading){
    TupleSB[][] verbos = new TupleSB[6][6];
    String line = null;
    int i, j;
    try{
      for(j = 0; j < 6; j++){
        for (i = 0; i < 3 ; i++) {
          line = walkOnFile(reading,"      <td class=\"output\">");
          verbos[i][j] = new TupleSB();
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
          verbos[i][j] = new TupleSB();
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

  private TupleSB[][] findConjuntivos(BufferedReader reading){
    TupleSB[][] verbos = new TupleSB[3][6];
    String line = null;
    int i, j;
    try{
      for(j = 0; j < 6; j++) {
        for (i = 0; i < 3 ; i++) {
          line = walkOnFile(reading,"      <td class=\"output\">");
          verbos[i][j] = new TupleSB();
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
      verbos[2][0] = new TupleSB();
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
        verbos[0][i] = new TupleSB();
        for(String text : this.splitFromSeparadores(this.dropTdClass(line, "<td.*%\">(.*)<span style="))){
          if(text.contains("<span class=\"irreg\">")){
            verbos[0][i].addConju(text.replaceAll("<span class=\"irreg\">", "").replaceAll("</span>", "").replaceAll(" ", ""),true);
          }else{
            verbos[0][i].addConju(text.replaceAll(" ", ""),false);
          }
        }
        /*Reading Negativo*/
        line = walkOnFile(reading,"      <td colspan=\"2\" class=\"output\"");
        verbos[1][i] = new TupleSB();
        for(String text : this.splitFromSeparadores(this.dropTdClass(line, "<td.*>.*<span style.*>n.o..?.?</span>(.*)<span style"))){
          if(text.contains("<span class=\"irreg\">")){
            verbos[1][i].addConju(text.replaceAll("<span class=\"irreg\">", "").replaceAll("</span>", "").replaceAll(" ", ""),true);
          }else{
            verbos[1][i].addConju(text.replaceAll(" ", ""),false);
          }
        }

        /*Reading Infinitivo Pessoal*/
        line = walkOnFile(reading,"      <td colspan=\"2\" class=\"output\"");
        verbos[2][i] = new TupleSB();
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
