/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infox.telas;

import java.sql.*;
import br.com.infox.dal.ModuloConexao;
import java.util.HashMap;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Tiago Aniceto
 */
public class TelaOS extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    private Object e;

    // a linha abaixo cria variável para armazenar um texto de acordo com o radion buttonselecionado
    private String tipo;

    /**
     * Creates new form TelaOS
     */
    public TelaOS() {
        initComponents();
        conexao = ModuloConexao.conector();
    }

    private void pesquisar_cliente() {
        String sql = "select idcli as Id, nomecli as Nome, fonecli as Fone from tbclientes where nomecli like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, TxtCliPesquisar.getText() + "%");
            rs = pst.executeQuery();
            TblClientes.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    private void setar_campos() {
        int setar = TblClientes.getSelectedRow();
        TxtCliId.setText(TblClientes.getModel().getValueAt(setar, 0).toString());

    }

    // método para cadastrar uma ordem de serviço
    private void emitir_os() {
        String sql = "insert into tbos (tipo,situacao,equipamento,defeito,servico,tecnico,valor,idcli) values (?,?,?,?,?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, tipo);
            pst.setString(2, CboOsSit.getSelectedItem().toString());
            pst.setString(3, TxtOsEquip.getText());
            pst.setString(4, TxtOsDef.getText());
            pst.setString(5, TxtOsServ.getText());
            pst.setString(6, TxtOsTec.getText());
            // .replace substitue a virgula pelo ponto
            pst.setString(7, TxtOsValor.getText().replace(",", "."));
            pst.setString(8, TxtCliId.getText());

            //validação dos campos obrigatórios
            //atalho if = ifel ctrl+space
            if ((TxtCliId.getText().isEmpty()) || (TxtOsEquip.getText().isEmpty()) || (TxtOsDef.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");

            } else {
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Ordem de Serviço emitida com sucesso");

                    TxtCliId.setText(null);
                    TxtOsEquip.setText(null);
                    TxtOsDef.setText(null);
                    TxtOsServ.setText(null);
                    TxtOsTec.setText(null);
                    TxtOsValor.setText(null);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // método para pesquisar uma Os
    private void pesquisar_os() {
        // a linha abaixo cria uma caixa de entrada do tipo JoptionPane
        String num_os = JOptionPane.showInputDialog("Número da OS");
        String sql = "select *from tbos where os=" + num_os;
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                TxtOs.setText(rs.getString(1));
                TxtData.setText(rs.getString(2));
                //setando os radio butons
                String rbtTipo = rs.getString(3);
                if (rbtTipo.equals("Os")) {
                    RbtOs.setSelected(true);
                    tipo = "Os";
                } else {
                    RbtOrc.setSelected(true);
                    tipo = "Orçamento";
                }
                CboOsSit.setSelectedItem(rs.getString(4));
                TxtOsEquip.setText(rs.getString(5));
                TxtOsDef.setText(rs.getString(6));
                TxtOsServ.setText(rs.getString(7));
                TxtOsTec.setText(rs.getString(8));
                TxtOsValor.setText(rs.getString(9));
                TxtCliId.setText(rs.getString(10));
                //evitando poblemas
                BtnOsAdicionar.setEnabled(false);
                TxtCliPesquisar.setEnabled(false);
                TblClientes.setVisible(false);

            } else {
                JOptionPane.showMessageDialog(null, "OS não cadastrada");
            }
        } catch (com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException e) {
            JOptionPane.showMessageDialog(null, "OS inválida");
            //System.out.println(e);
        } catch (Exception e2) {
            JOptionPane.showMessageDialog(null, e2);
        }
    }

    //método para aletrar uma OS
    private void alterar_os() {
        String sql = "update tbos set tipo=?,situacao=?,equipamento=?,defeito=?,servico=?,tecnico=?,valor=? where os=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, tipo);
            pst.setString(2, CboOsSit.getSelectedItem().toString());
            pst.setString(3, TxtOsEquip.getText());
            pst.setString(4, TxtOsDef.getText());
            pst.setString(5, TxtOsServ.getText());
            pst.setString(6, TxtOsTec.getText());
            // .replace substitue a virgula pelo ponto
            pst.setString(7, TxtOsValor.getText().replace(",", "."));
            pst.setString(8, TxtOs.getText());

            //validação dos campos obrigatórios
            //atalho if = ifel ctrl+space
            if ((TxtCliId.getText().isEmpty()) || (TxtOsEquip.getText().isEmpty()) || (TxtOsDef.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");

            } else {
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Ordem de Serviço alterada com sucesso");

                    TxtOs.setText(null);
                    TxtData.setText(null);
                    TxtCliId.setText(null);
                    TxtOsEquip.setText(null);
                    TxtOsDef.setText(null);
                    TxtOsServ.setText(null);
                    TxtOsTec.setText(null);
                    TxtOsValor.setText(null);
                    //habilitar os objetos
                    BtnOsAdicionar.setEnabled(true);
                    TxtCliPesquisar.setEditable(true);
                    TblClientes.setVisible(true);

                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

// método para excluir uma os
    private void excluir_os() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir esta OS ?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {

            String sql = "delete from tbos where os=?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, TxtOs.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Ordem de serviço excluida com sucesso ");

                    TxtOs.setText(null);
                    TxtData.setText(null);
                    TxtCliId.setText(null);
                    TxtOsEquip.setText(null);
                    TxtOsDef.setText(null);
                    TxtOsServ.setText(null);
                    TxtOsTec.setText(null);
                    TxtOsValor.setText(null);
                    //habilitar os objetos
                    BtnOsAdicionar.setEnabled(true);
                    TxtCliPesquisar.setEditable(true);
                    TblClientes.setVisible(true);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    // método para imprimir uma os
    private void imprimir_os() {

        // imprimindo uma os
        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a impressão desta OS ?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            // emitindo o relatório com frame work jasperReport
            try {
                //usando a classe HashMap para criar um filtro
                HashMap filtro = new HashMap();
                filtro.put("os", Integer.parseInt(TxtOs.getText()));
                //usando a classe JasperPrint para preparar a impressão de um relatório
                JasperPrint print = JasperFillManager.fillReport("C:/reports/os.jasper", filtro, conexao);
                // a linha abaixo exibe o relatório atraves da classe JasperViewer
                JasperViewer.viewReport(print, false);
            } catch (Exception e) {

                JOptionPane.showMessageDialog(null, e);
            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        TxtOs = new javax.swing.JTextField();
        TxtData = new javax.swing.JTextField();
        RbtOrc = new javax.swing.JRadioButton();
        RbtOs = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        CboOsSit = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        TxtCliPesquisar = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        TxtCliId = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        TblClientes = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        TxtOsEquip = new javax.swing.JTextField();
        TxtOsDef = new javax.swing.JTextField();
        TxtOsValor = new javax.swing.JTextField();
        TxtOsServ = new javax.swing.JTextField();
        TxtOsTec = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        BtnOsPesquisar = new javax.swing.JButton();
        BtnOsAlterar = new javax.swing.JButton();
        BtnOsExcluir = new javax.swing.JButton();
        BtnOsAdicionar = new javax.swing.JButton();
        BtnOsImprimir = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Ordem de serviço");
        setPreferredSize(new java.awt.Dimension(640, 480));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("N° OS");

        jLabel2.setText("Data");

        TxtOs.setEditable(false);

        TxtData.setEditable(false);
        TxtData.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N

        buttonGroup1.add(RbtOrc);
        RbtOrc.setText("Orçamento");
        RbtOrc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RbtOrcActionPerformed(evt);
            }
        });

        buttonGroup1.add(RbtOs);
        RbtOs.setText("Ordem de serviço");
        RbtOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RbtOsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(TxtOs, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(45, 45, 45)
                                .addComponent(jLabel2)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(TxtData)
                                .addContainerGap())))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(RbtOrc)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(RbtOs)
                        .addGap(35, 35, 35))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TxtOs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TxtData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RbtOrc)
                    .addComponent(RbtOs))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jLabel3.setText("Situação");

        CboOsSit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Na bancada", "Entrega OK", "Orçamento Reprovado", "Aguardando Aprovação", "Aguardando Peças", "Abandonado pelo cliente", "Retornou", " " }));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Cliente"));

        TxtCliPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtCliPesquisarActionPerformed(evt);
            }
        });
        TxtCliPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TxtCliPesquisarKeyReleased(evt);
            }
        });

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/search.png"))); // NOI18N

        jLabel5.setText("* id");

        TxtCliId.setEditable(false);

        TblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "id", "Nome", "Fone"
            }
        ));
        TblClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TblClientesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TblClientes);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(TxtCliPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(TxtCliId, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(TxtCliId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4)
                    .addComponent(TxtCliPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel6.setText("*Equipamento");

        jLabel7.setText("*Defeito");

        jLabel8.setText("Serviço");

        jLabel9.setText("Técnico");

        TxtOsValor.setText("0");

        jLabel10.setText("Valor Total");

        BtnOsPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/read.png"))); // NOI18N
        BtnOsPesquisar.setPreferredSize(new java.awt.Dimension(80, 80));
        BtnOsPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnOsPesquisarActionPerformed(evt);
            }
        });

        BtnOsAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/update.png"))); // NOI18N
        BtnOsAlterar.setPreferredSize(new java.awt.Dimension(80, 80));
        BtnOsAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnOsAlterarActionPerformed(evt);
            }
        });

        BtnOsExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/delete.png"))); // NOI18N
        BtnOsExcluir.setPreferredSize(new java.awt.Dimension(80, 80));
        BtnOsExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnOsExcluirActionPerformed(evt);
            }
        });

        BtnOsAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/create.png"))); // NOI18N
        BtnOsAdicionar.setPreferredSize(new java.awt.Dimension(80, 80));
        BtnOsAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnOsAdicionarActionPerformed(evt);
            }
        });

        BtnOsImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icones/impressora.png"))); // NOI18N
        BtnOsImprimir.setToolTipText("Imprimir OS");
        BtnOsImprimir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BtnOsImprimir.setPreferredSize(new java.awt.Dimension(80, 80));
        BtnOsImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnOsImprimirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(BtnOsAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(BtnOsPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(BtnOsAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(BtnOsExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(BtnOsImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(TxtOsTec, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(TxtOsValor, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(TxtOsDef, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)
                            .addComponent(TxtOsEquip, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TxtOsServ))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(CboOsSit, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(46, 46, 46))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(CboOsSit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel7))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(TxtOsEquip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(TxtOsDef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TxtOsServ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel9))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(TxtOsTec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(TxtOsValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BtnOsImprimir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(BtnOsAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(BtnOsPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(BtnOsExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(BtnOsAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(60, Short.MAX_VALUE))
        );

        setBounds(0, 0, 640, 480);
    }// </editor-fold>//GEN-END:initComponents

    private void TxtCliPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtCliPesquisarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtCliPesquisarActionPerformed

    private void TxtCliPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TxtCliPesquisarKeyReleased
        // chamando o método pesquisar_classe
        pesquisar_cliente();
    }//GEN-LAST:event_TxtCliPesquisarKeyReleased

    private void TblClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TblClientesMouseClicked
        // chamando o método setar campos
        setar_campos();
    }//GEN-LAST:event_TblClientesMouseClicked

    private void RbtOrcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RbtOrcActionPerformed
        //  atribuindo um texto a variável "tipo" se o radio buton estiver selecionado
        tipo = "Orçamento";
    }//GEN-LAST:event_RbtOrcActionPerformed

    private void RbtOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RbtOsActionPerformed
        // atribuindo um texto a variável "tipo" se selecionado
        tipo = "Os";
    }//GEN-LAST:event_RbtOsActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // ao abrir o form marcar o radio buton orçamento
        RbtOrc.setSelected(true);
        tipo = "Orçamento";

    }//GEN-LAST:event_formInternalFrameOpened

    private void BtnOsAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnOsAdicionarActionPerformed
        // chamar o método emitit_os
        emitir_os();
    }//GEN-LAST:event_BtnOsAdicionarActionPerformed

    private void BtnOsPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnOsPesquisarActionPerformed
        // chamando o método pesquisar_os
        pesquisar_os();
    }//GEN-LAST:event_BtnOsPesquisarActionPerformed

    private void BtnOsAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnOsAlterarActionPerformed
        // chamando o método alterar_os
        alterar_os();
    }//GEN-LAST:event_BtnOsAlterarActionPerformed

    private void BtnOsExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnOsExcluirActionPerformed
        // chamando método para uma excluir_os
        excluir_os();
    }//GEN-LAST:event_BtnOsExcluirActionPerformed

    private void BtnOsImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnOsImprimirActionPerformed
        // chamando o método para imprimir uma OS
        imprimir_os();
    }//GEN-LAST:event_BtnOsImprimirActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnOsAdicionar;
    private javax.swing.JButton BtnOsAlterar;
    private javax.swing.JButton BtnOsExcluir;
    private javax.swing.JButton BtnOsImprimir;
    private javax.swing.JButton BtnOsPesquisar;
    private javax.swing.JComboBox<String> CboOsSit;
    private javax.swing.JRadioButton RbtOrc;
    private javax.swing.JRadioButton RbtOs;
    private javax.swing.JTable TblClientes;
    private javax.swing.JTextField TxtCliId;
    private javax.swing.JTextField TxtCliPesquisar;
    private javax.swing.JTextField TxtData;
    private javax.swing.JTextField TxtOs;
    private javax.swing.JTextField TxtOsDef;
    private javax.swing.JTextField TxtOsEquip;
    private javax.swing.JTextField TxtOsServ;
    private javax.swing.JTextField TxtOsTec;
    private javax.swing.JTextField TxtOsValor;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
