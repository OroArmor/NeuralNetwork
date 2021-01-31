package com.oroarmor.neural_network.lib.network;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Collectors;

import com.oroarmor.neural_network.lib.layer.Layer;
import com.oroarmor.neural_network.lib.matrix.CPUMatrix;
import com.oroarmor.neural_network.lib.matrix.Matrix;
import com.oroarmor.neural_network.lib.training.models.TrainingModel;

public class NeuralNetwork<T extends Matrix<T>> implements Serializable {
	private static final long serialVersionUID = 1L;

	ArrayList<Layer<T>> layers;
	int inputs;
	int trains;

	public NeuralNetwork(int inputNeurons) {
		inputs = inputNeurons;
		layers = new ArrayList<>();
	}

	public void addLayer(Layer<T> layer) {
		if (layers.isEmpty()) {
			layer.setup(inputs);
		} else {
			layer.setup(layers.get(layers.size() - 1).getOutputNeurons());
		}
		layers.add(layer);
	}

	public T feedFoward(T inputs) {
		for (Layer<T> layer : layers) {
			inputs = layer.feedFoward(inputs);
		}
		return inputs;
	}

	public Layer<T> getLayer(int layerIndex) {
		return layers.get(layerIndex);
	}

	public int getTrainingAttemps() {
		return trains;
	}

	@SuppressWarnings("unchecked")
	public synchronized void train(T input, T output, TrainingModel model) {
		trains++;

		T[] layerOutputs = (T[]) new Matrix[layers.size()];
		int i = 0;
		for (Layer<T> layer : layers) {
			if (i == 0) {
				layerOutputs[i] = layer.feedFoward(input);
			} else {
				layerOutputs[i] = layer.feedFoward(layerOutputs[i - 1]);
			}

			i++;
		}

		model.fixErrors(layers, layerOutputs, output, input);
	}

	public NeuralNetwork<CPUMatrix> convertAllToCPU() {
		NeuralNetwork<CPUMatrix> newNetwork = new NeuralNetwork<>(inputs);
		newNetwork.trains = this.trains;
		newNetwork.layers = (ArrayList<Layer<CPUMatrix>>) layers.stream().map(Layer::contvertToCPU)
				.collect(Collectors.toList());

		return newNetwork;
	}
}