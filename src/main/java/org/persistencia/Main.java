package org.persistencia;

import org.persistencia.service.UniversidadeServico;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UniversidadeServico universidadeServico = UniversidadeServico.getInstance();
        Scanner sc = new Scanner(System.in);
        int escolha = 0;

        while (escolha != 5){
            mostrarMenu();
            String input = sc.nextLine();

            try{
                escolha = Integer.parseInt(input);
            }
            catch (Exception e){
                System.out.println("Opção inválida!");
            }

            switch (escolha){
                case 1:
                    System.out.print("Digite o Nome: ");
                    input = sc.nextLine();
                    universidadeServico.buscarPorNome(input);
                    break;
                case 2:
                    System.out.println("BUSCA POR NOME E ANO");
                    break;
                case 3:
                    System.out.println("LISTAR TODOS OS CURSOS");
                    break;
                case 4:
                    System.out.println("EXPORTAR PARA O BANCO");
                    break;
                case 5:
                    break;
            }
        }

    }

    private static void mostrarMenu(){
        System.out.println("--------------- MENU -------------------");
        System.out.println("1 - Pesquisa por Nome");
        System.out.println("2 - Pesquisa por Ano e Nome");
        System.out.println("3 - Listar Todos os Cursos");
        System.out.println("4 - Exportar para um banco de dados");
        System.out.println();
        System.out.println("5 - Sair");
        System.out.println("----------------------------------------");
        System.out.print("Escolha: ");
    }
}