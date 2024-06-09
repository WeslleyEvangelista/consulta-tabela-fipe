package br.com.alura.TabelaFipe.main;

import br.com.alura.TabelaFipe.model.Data;
import br.com.alura.TabelaFipe.model.VehicleModels;
import br.com.alura.TabelaFipe.service.ApiConsumption;
import br.com.alura.TabelaFipe.service.ConvertData;

import java.util.Comparator;
import java.util.Scanner;

public class Main {
    private Scanner scanner = new Scanner(System.in);
    private final String BASE_URL = "https://parallelum.com.br/fipe/api/v1/";
    private ApiConsumption apiConsumption = new ApiConsumption();
    private ConvertData conversor = new ConvertData();

    public void displayMenu() {
        var menu = """
                *** Modelos de Veículos ***
                Carro
                Moto
                Caminhão
                
                Digite o tipo de veículo que deseja consultar:
                """;
        System.out.println(menu);

        var option = scanner.nextLine();
        String url;

        if (option.toLowerCase().contains("carr")) {
            url = BASE_URL + "carros/marcas";
        } else if (option.toLowerCase().contains("mot")) {
            url = BASE_URL + "motos/marcas";
        } else {
            url = BASE_URL + "caminhoes/marcas";
        }

        var json = apiConsumption.getData(url);
        System.out.println(json);
        var marcasVeiculo = conversor.getList(json, Data.class);
        marcasVeiculo.stream()
                .sorted(Comparator.comparing(Data::codigo))
                .forEach(System.out::println);

        System.out.println("Informe o código da marca do veículo: ");
        var codigoMarcaVeiculo = scanner.nextLine();


        url = url + "/" + codigoMarcaVeiculo + "/modelos";
        json = apiConsumption.getData(url);
        var modeloLista = conversor.getData(json, VehicleModels.class);

        System.out.println("\nModelos dessa marca: ");
        modeloLista.modelos().stream()
                .sorted(Comparator.comparing(Data::codigo))
                .forEach(System.out::println);

    }
}
