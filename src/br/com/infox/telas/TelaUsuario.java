/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infox.telas;

/**
 * Aula 13
 *
 * @author Tiago Aniceto
 */
//
import java.sql.*;
import br.com.infox.dal.ModuloConexao;//importar o modulo de conexao
import javax.swing.JOptionPane;

public class TelaUsuario extends javax.swing.JInternalFrame {

    // usando a variavel da conexao do DAL
    // todo formulario que eu criar e ligar ao banco de dados , tem que fazer o mesmo processo 
    Connection conexao = null;// tem haver com o modulo de conexao
    PreparedStatement pst = null;//prepara a conexão com o banco
    ResultSet rs = null;//exibe o resultado da conexão com o bamco

    public TelaUsuario() {//construtor
        initComponents();
        conexao = ModuloConexao.conector();// chamando o modulo de conexão

    }

    // Este processo sempre vai ser feito tbm , quando for 
    //ligar ao banco
    //criando o metodo consultar
    // modificador private permite que o metodo seja executado aqui dentro
    // e void porque é um metodo sem retorno
    private void consultar() {
        String sql = "select * from tbusuarios where iduser=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, TxtUsuId.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                TxtUsuNome.setText(rs.getString(2));
                TxtUsuFone.setText(rs.getString(3));
                TxtUsuLogin.setText(rs.getString(4));
                TxtUsuSenha.setText(rs.getString(5));
                //a linhs abaixo se refere ao combobox
                //qualquer objeto usando essa lógica eu posso trazer as imformações
                //do banco de dados e substituir no meu formulário , nesse caso caixa de texto e combobox
                CboUsuPerfil.setSelectedItem(rs.getString(6));
            } else {
                JOptionPane.showMessageDialog(null, "Usuário não cadastrado");
                // as linhas abaixo "limpam os campos"
                TxtUsuNome.setText(null);
                TxtUsuFone.setText(null);
                TxtUsuLogin.setText(null);
                TxtUsuSenha.setText(null);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);// exibe a excessão 
        }
    }
    // método para adicionar usuários

    private void adicionar() {
        String sql = "insert into tbusuarios(iduser,usuario,fone,login,senha,perfil) Values(?,?,?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, TxtUsuId.getText());
            pst.setString(2, TxtUsuNome.getText());
            pst.setString(3, TxtUsuFone.getText());
            pst.setString(4, TxtUsuLogin.getText());
            pst.setString(5, TxtUsuSenha.getText());
            pst.setString(6, CboUsuPerfil.getSelectedItem().toString());
            //validação dos campos obrigatórios
            if ((TxtUsuId.getText().isEmpty()) || (TxtUsuNome.getText().isEmpty()) || (TxtUsuLogin.getText().isEmpty()) || (TxtUsuSenha.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {

                // a linha abaixo atualiza a tabela usuario com os dados do formulário
                // a estrutura abaixo é usada  para confimar a inserção dos dados  na tabela
                int adicionado = pst.executeUpdate();
                // a linha abaixo serve como apoio para entendimento da lógica
                //System.out.println(adicionado); mostra no console uma linha adicionada
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Usuário adicionado com sucesso");
                    TxtUsuId.setText(null);
                    TxtUsuNome.setText(null);
                    TxtUsuFone.setText(null);
                    TxtUsuLogin.setText(null);
                    TxtUsuSenha.setText(null);

                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //criando o método para alterar dados do usuário
    private void alterar() {
        String sql = "update tbusuarios set usuario=?,fone=?,login=?,senha=?,perfil=? where iduser=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, TxtUsuNome.getText());
            pst.setString(2, TxtUsuFone.getText());
            pst.setString(3, TxtUsuLogin.getText());
            pst.setString(4, TxtUsuSenha.getText());
            pst.setString(5, CboUsuPerfil.getSelectedItem().toString());
            pst.setString(6, TxtUsuId.getText());
            if ((TxtUsuId.getText().isEmpty()) || (TxtUsuNome.getText().isEmpty()) || (TxtUsuLogin.getText().isEmpty()) || (TxtUsuSenha.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {

                // a linha abaixo atualiza a tabela usuario com os dados do formulário
                // a estrutura abaixo é usada  para confimar a alteração dos dados  na tabela
                int adicionado = pst.executeUpdate();
                // a linha abaixo serve como apoio para entendimento da lógica
                //System.out.println(adicionado); mostra no console uma linha adicionada
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Dados do usuário alterados com sucesso");
                    //e aqui ele limpa os campos
                    TxtUsuId.setText(null);
                    TxtUsuNome.setText(null);
                    TxtUsuFone.setText(null);
                    TxtUsuLogin.setText(null);
                    TxtUsuSenha.setText(null);

                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // método responsável pela remoção de usuário
    private void remover() {
        //a estrutura confima a remoção do usuário
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover este usuário ?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_NO_OPTION) {
            String sql = "delete from tbusuarios where iduser=?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, TxtUsuId.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Usuário Removido com sucesso");
                    TxtUsuId.setText(null);
                    TxtUsuNome.setText(null);
                    TxtUsuFone.setText(null);
                    TxtUsuLogin.setText(null);
                    TxtUsuSenha.setText(null);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        TxtUsuId = new javax.swing.JTextField();
        TxtUsuNome = new javax.swing.JTextField();
        TxtUsuFone = new javax.swing.JTextField();
        CboUsuPerfil = new javax.swing.JComboBox<>();
        TxtUsuLogin = new javax.swing.JTextField();
        TxtUsuSenha = new javax.swing.JTextField();
        BtnUsuCreate = new javax.swing.JButton();
        BtnUsuRead = new javax.swing.JButton();
        BtnUsuUpdate = new javax.swing.JButton();
        BtnUsuDelete = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Usuários");
        setPreferredSize(new java.awt.Dimension(640, 480));

        jLabel1.setText("*id");

        jLabel2.setText("*Nome");

        jLabel3.setText("Fone");

        jLabel4.setText("*Login");

        jLabel5.setText("*Senha");

        jLabel6.setText("*Perfil");

        TxtUsuFone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtUsuFoneActionPerformed(evt);
            }
        });

        CboUsuPerfil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "admin", "user" }));

        BtnUsuCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/create.png"))); // NOI18N
        BtnUsuCreate.setToolTipText("Adicionar");
        BtnUsuCreate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BtnUsuCreate.setPreferredSize(new java.awt.Dimension(80, 80));
        BtnUsuCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnUsuCreateActionPerformed(evt);
            }
        });

        BtnUsuRead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/read.png"))); // NOI18N
        BtnUsuRead.setToolTipText("Consultar");
        BtnUsuRead.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BtnUsuRead.setPreferredSize(new java.awt.Dimension(80, 80));
        BtnUsuRead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnUsuReadActionPerformed(evt);
            }
        });

        BtnUsuUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/update.png"))); // NOI18N
        BtnUsuUpdate.setToolTipText("Alterar");
        BtnUsuUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BtnUsuUpdate.setPreferredSize(new java.awt.Dimension(80, 80));
        BtnUsuUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnUsuUpdateActionPerformed(evt);
            }
        });

        BtnUsuDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/delete.png"))); // NOI18N
        BtnUsuDelete.setToolTipText("Remover");
        BtnUsuDelete.setPreferredSize(new java.awt.Dimension(80, 80));
        BtnUsuDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnUsuDeleteActionPerformed(evt);
            }
        });

        jLabel7.setText("*Campos obrigatórios");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(TxtUsuNome, javax.swing.GroupLayout.PREFERRED_SIZE, 486, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(TxtUsuId, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel7)
                                .addGap(43, 43, 43))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(12, 12, 12)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TxtUsuFone, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TxtUsuSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(38, 38, 38)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(CboUsuPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(TxtUsuLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(51, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(BtnUsuCreate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(BtnUsuRead, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(BtnUsuUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(BtnUsuDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(79, 79, 79))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TxtUsuId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel7))
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(TxtUsuNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(TxtUsuFone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(TxtUsuLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TxtUsuSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(CboUsuPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(53, 53, 53)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BtnUsuCreate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnUsuRead, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnUsuUpdate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtnUsuDelete, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(86, Short.MAX_VALUE))
        );

        setBounds(0, 0, 640, 480);
    }// </editor-fold>//GEN-END:initComponents

    private void BtnUsuReadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnUsuReadActionPerformed
        // chamando o método consultar
        consultar();
    }//GEN-LAST:event_BtnUsuReadActionPerformed

    private void BtnUsuCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnUsuCreateActionPerformed
        // chamando o método adicionar
        adicionar();

    }//GEN-LAST:event_BtnUsuCreateActionPerformed

    private void TxtUsuFoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtUsuFoneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtUsuFoneActionPerformed

    private void BtnUsuUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnUsuUpdateActionPerformed
        // chamando o método alterar
        alterar();
    }//GEN-LAST:event_BtnUsuUpdateActionPerformed

    private void BtnUsuDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnUsuDeleteActionPerformed
        // chamando o método remover
        remover();
    }//GEN-LAST:event_BtnUsuDeleteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnUsuCreate;
    private javax.swing.JButton BtnUsuDelete;
    private javax.swing.JButton BtnUsuRead;
    private javax.swing.JButton BtnUsuUpdate;
    private javax.swing.JComboBox<String> CboUsuPerfil;
    private javax.swing.JTextField TxtUsuFone;
    private javax.swing.JTextField TxtUsuId;
    private javax.swing.JTextField TxtUsuLogin;
    private javax.swing.JTextField TxtUsuNome;
    private javax.swing.JTextField TxtUsuSenha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    // End of variables declaration//GEN-END:variables
}
