package com.company;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

class Data{
    private int Dia;
    private int Mes;
    private int Ano;
    Data(int dia, int mes, int ano){
        this.Dia = dia;
        this.Mes = mes;
        this.Ano = ano;
    }
    public String formatData(){
        return ""+this.Dia+"/"+this.Mes+"/"+this.Ano;
    }
}

interface Autorizavel{
    boolean autoriza(int senha);
}

class Funcionario{
    private static int totalFuncionarios = 0;

    public static void addFuncionario() {
        Funcionario.totalFuncionarios += 1;
    }

    public static int getTotalFuncionarios() {
        return totalFuncionarios;
    }

    private int id;
    private String nome;
    private String sobrenome;
    private int idade;
    private Data data_Cadastro;
    protected Funcionario(String nome, String sobrenome, int idade,int dia, int mes, int ano){
        addFuncionario();
        this.id = getTotalFuncionarios();
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.idade = idade;
        this.data_Cadastro = new Data(dia, mes, ano);
    }

    public String getNome() {
        return this.nome;
    }

    public String getSobrenome(){
        return this.sobrenome;
    }
}

class Gerente extends Funcionario implements Autorizavel{
    private static ArrayList<Gerente> listaGerentes = new ArrayList<>();

    public static ArrayList<Gerente> getListaGerentes() {
        return listaGerentes;
    }

    public static void addListaGerentes(Gerente g) {
        Gerente.listaGerentes.add(g);
    }

    private int senha;
    Gerente(String nome, String sobrenome, int idade,int dia, int mes, int ano, int senha){
        super(nome,sobrenome,idade,dia,mes,ano);
        this.senha = senha;
    }

    @Override
    public boolean autoriza(int senha) {
        if (senha == this.senha){
            return true;
        }
        return false;
    }
}

class Vendedor extends Funcionario implements Autorizavel{
    private static ArrayList<Vendedor> listaVendedores = new ArrayList<>();

    public static ArrayList<Vendedor> getListaVendedores() {
        return listaVendedores;
    }

    public static void addListaVendedores(Vendedor v) {
        Vendedor.listaVendedores.add(v);
    }
    private int senha;
    Vendedor(String nome, String sobrenome, int idade,int dia, int mes, int ano, int senha){
        super(nome,sobrenome,idade,dia,mes,ano);
        this.senha = senha;
    }

    @Override
    public boolean autoriza(int senha) {
        if (senha == this.senha){
            return true;
        }
        return false;
    }
}

class Produto{
    private String tipo_Produto;
    private String marca;
    private String cor_Produto;
    private String descricao_Produto;
    private double valor_Produto;
    Produto(String tipo, String cor,String marca,double valor){
        this.tipo_Produto = tipo;
        this.cor_Produto = cor;
        this.marca = marca;
        this.descricao_Produto = this.tipo_Produto+" "+this.cor_Produto+" "+this.marca;
        this.valor_Produto = valor;
    }

    public double getValor_Produto() {
        return valor_Produto;
    }

    public String getDescricao_Produto() {
        return descricao_Produto;
    }

    public String toString(int qnt) {
        DecimalFormat df = new DecimalFormat("#.00");
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter d = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");
        String data = ldt.format(d);
        return data+" "+getDescricao_Produto()+" x"+qnt+" .......... "+df.format(getValor_Produto()*qnt);
    }
}

class Loja{
    private HashMap<String,Produto> produtoMap = new HashMap<>();
    public void addProduto(String tipo, String cor,String marca,double valor){
        Produto tmp = new Produto(tipo, cor, marca, valor);
        this.produtoMap.put(tmp.getDescricao_Produto(),tmp);
    }
    private void removerProduto(String key){
        produtoMap.remove(key);
    }
    private Set<String> retornarProdutos(){
       return this.produtoMap.keySet();
    }
    public void Menu(){
        Funcionario logado = null;
        while (true){
            Scanner s = new Scanner(System.in);
            s.reset();
            System.out.println("0 - Sair \n1 - Entrar como Gerente \n2 - Entrar como Vendedor");
            switch (Integer.parseInt(s.nextLine())){
                case 0:
                    return;
                case 1:
                    int i = 0;
                    int opg;
                    if (!(logado instanceof Gerente) || logado == null){
                        ArrayList<Gerente> tmg = Gerente.getListaGerentes();
                        System.out.println("Escolha a conta para logar:. ");
                        for(Gerente g : tmg){
                            System.out.println("["+i+"] "+g.getNome()+" "+g.getSobrenome());
                            i++;
                        }
                        i = 0;

                        opg = Integer.parseInt(s.nextLine());;
                        for(Gerente g : tmg){
                            if (i == opg){
                                System.out.print("Digite a senha :. ");
                                int senha = Integer.parseInt(s.nextLine());
                                if (g.autoriza(senha)){
                                    logado = g;
                                }else{
                                    System.out.println("Senha Incorreta");
                                }
                                break;
                            }
                            i++;
                        }
                        if (i > tmg.size()){
                            System.out.println("Opção Inválida");

                        }
                    }
                    if (logado instanceof Gerente){
                        System.out.println("Bem Vindo, "+logado.getNome());
                        System.out.println("0 - Sair\n1 - Adicionar Produtos\n2 - Remover Produtos");
                        opg = Integer.parseInt(s.nextLine());
                        switch (opg){
                            case 0:
                                return;
                            case 1:
                                String tipo;
                                String cor;
                                String marca;
                                double valor;
                                System.out.println("Tipo de Produto :. ");
                                tipo = s.nextLine();
                                System.out.println("Cor do Produto :. ");
                                cor = s.nextLine();
                                System.out.println("Marca do produto:. ");
                                marca = s.nextLine();
                                System.out.println("Valor do Produto:. ");
                                valor = Double.parseDouble(s.nextLine());
                                addProduto(tipo,cor,marca,valor);
                                break;
                            case 2:
                                i = 0;
                                for(String x : retornarProdutos()){
                                    System.out.println("["+i+"] "+x);
                                    i++;
                                }
                                i = 0;
                                opg = s.nextInt();
                                for(String x : retornarProdutos()){
                                    if (opg == i){
                                        removerProduto(x);
                                        break;
                                    }
                                    i++;
                                }
                                System.out.println(retornarProdutos());
                                if (i > retornarProdutos().size()){
                                    System.out.println("Inválido");
                                }
                                break;
                        }
                    }
                    break;
                case 2:
                    i = 0;
                    if (!(logado instanceof Vendedor) || logado == null){
                        ArrayList<Vendedor> tmp = Vendedor.getListaVendedores();
                        System.out.println("Escolha a conta para logar:. ");
                        for(Vendedor v : tmp){
                            System.out.println("["+i+"] "+v.getNome()+" "+v.getSobrenome());
                            i++;
                        }
                        i = 0;
                        int opc = s.nextInt();
                        for(Vendedor v : tmp){
                            if (i == opc){
                                System.out.print("Digite a senha :. ");
                                int senha = s.nextInt();
                                if (v.autoriza(senha)){
                                    logado = v;
                                }
                            }
                            i++;
                        }
                        if (i > tmp.size()){
                            System.out.println("Opção Inválida");
                        }
                    }
                    if (logado instanceof Vendedor){
                        ArrayList<String> carrinho = null;
                        System.out.println("Bem Vindo, "+logado.getNome());
                        carrinho = compras();
                        if (carrinho != null){
                            System.out.println("Vendedor :. "+logado.getNome()+" "+logado.getSobrenome());
                            System.out.println("---------------------------------------------------------");
                            for (String x : carrinho){
                                System.out.println(x);
                            }
                        }
                    }

                    break;
                default:
                    System.out.println("Opção Inválida");
                    break;

            }

        }

    }
    private ArrayList<String> compras(){
        int cont = 0;
        double valorTotal = 0;
        ArrayList<String> compras = new ArrayList<>();
        while(true){
            int i = 1;
            Set<String> tm= retornarProdutos();
            System.out.println("[0] Sair");
            for (String x : tm){
                System.out.println("["+i+"] "+x);
                i++;
            }
            Scanner s = new Scanner(System.in);

            i = 0;
            int op = s.nextInt();
            Produto tmp = null;
            if (op == 0){
                double desc = 0;
                if (cont >= 10 && cont < 15){
                    desc = 5;
                }else if (cont >= 15 && cont < 20){
                    desc = 10;
                }else if (cont >= 20){
                    desc = 20;
                }
                DecimalFormat df = new DecimalFormat("#.00");
                compras.add("Valor Total(Sem Desconto) ........................ R$"+df.format(valorTotal));
                if (desc > 0){
                    valorTotal -= (desc/100 * valorTotal);
                }
                compras.add("Desconto ......................................... "+desc+"%");
                compras.add("Valor Total(Com Desconto) ........................ R$"+df.format(valorTotal));
                return compras;
            }else{
                op -= 1;
                for (String x : tm){
                    if (i == op){
                        op = 0;
                        System.out.print("Insira a Quantidade:. ");
                        while (op == 0) {
                            op = s.nextInt();
                            if (op > 0){

                                tmp = produtoMap.get(x);
                                compras.add(tmp.toString(op));
                                valorTotal += tmp.getValor_Produto()*op;
                                cont += op;
                            }
                        }
                        break;
                    }
                    i++;
                }
                if (i > tm.size()){
                    System.out.println("Opção Inválida");
                }

            }

        }

    }
}

public class Main {

    public static void main(String[] args) {
        Gerente.addListaGerentes(new Gerente("marcelo","gonzaga",17,10,12,2019,1234));
        Vendedor.addListaVendedores(new Vendedor("marcelo","gonzaga",18,10,12,2019,4321));
        Loja l = new Loja();
        l.addProduto("Relógio","Dourado","nSei",125.74);
        l.addProduto("Relógio","Cinza","nSei",130);
        l.Menu();


    }
}