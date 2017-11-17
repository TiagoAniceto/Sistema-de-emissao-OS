/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infox.dal;

import java.sql.*;

/**
 *
 * @author pure.TI
 */
public class ModuloConexao {
    //metodo responsavel por estabelecer a conexao com o banco
    //o connection é um framework,que vem com um pacote do java.sql
    //conector é o nome do meu metodo
    //sempre que eu crio um medoto sem a palvra void,ele tem que ter retorno
    public static Connection conector(){
        java.sql.Connection conexao = null;
          // a linha abaixo chama o driver que eu importei para a biblioteca
          String driver = "com.mysql.jdbc.Driver";
           // Armazendo informações refenrentes ao banco
           String url = "jdbc:mysql://localhost:3306/db_infox";
           String user= "root";
           String password = "";
           // Estabelecendo a conexão com o banco
           try {
              Class.forName(driver);
              conexao = DriverManager.getConnection(url, user, password);
              return conexao;
        } catch (Exception e) {
               // a linha abaixo serve ded apoio para esclarecer o erro
               //System.out.println(e);
            return null;
        }
               
    }
    
}
