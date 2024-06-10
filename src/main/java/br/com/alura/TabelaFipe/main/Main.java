package br.com.alura.TabelaFipe.main;

import br.com.alura.TabelaFipe.model.Data;
import br.com.alura.TabelaFipe.model.Vehicle;
import br.com.alura.TabelaFipe.model.VehicleModels;
import br.com.alura.TabelaFipe.service.ApiConsumption;
import br.com.alura.TabelaFipe.service.ConvertData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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

        System.out.println("\nDigite um trecho do nome do carro a ser pesquisado:");
        var nomeVeiculo = scanner.nextLine();

        List<Data> modelosFiltrados = modeloLista.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
                .collect(Collectors.toList());

        System.out.println("\nModelos filtrados:");
        modelosFiltrados.forEach(System.out::println);

        System.out.println("Digite por favor o código do modelo para buscar os valores:");
        var codigoModelo = scanner.nextLine();

        url = url + "/" + codigoModelo + "/anos";
        json = apiConsumption.getData(url);
        List<Data> years = conversor.getList(json, Data.class);
        List<Vehicle> vehicles = new ArrayList<>();

        for (int i = 0; i < years.size(); i++) {
            var yearsUrl = url + "/" + years.get(i).codigo();
            json = apiConsumption.getData(yearsUrl);
            Vehicle vehicle = conversor.getData(json, Vehicle.class);
            vehicles.add(vehicle);
        }

        System.out.println("\nTodos os veículos filtrados com avaliações por ano: ");
        vehicles.forEach(System.out::println);
    }
}
