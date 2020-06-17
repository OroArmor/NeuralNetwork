package oroarmor.neuralnetwork.layer;

import java.util.Random;

import oroarmor.neuralnetwork.matrix.CPUMatrix;
import oroarmor.neuralnetwork.matrix.Matrix;
import oroarmor.neuralnetwork.matrix.Matrix.MatrixType;
import oroarmor.neuralnetwork.matrix.function.MatrixFunction;
import oroarmor.neuralnetwork.matrix.function.SigmoidMatrix;

@SuppressWarnings("unchecked")
public class FeedFowardLayer<T extends Matrix<T>> extends Layer<T> {

	private static final long serialVersionUID = 12L;

	int previousNeurons;
	T weights;

	public FeedFowardLayer(int neurons, MatrixType type) {
		super(neurons, type);
	}

	@Override
	public T feedFoward(T inputs) {
		return weights.multiplyMatrix(inputs).applyFunction(new SigmoidMatrix()); // sig(W*I)
	}

	@Override
	public MatrixFunction getMatrixFunction() {
		return new SigmoidMatrix();
	}

	@Override
	public int getOutputNeurons() {
		return neurons;
	}

	@Override
	public T[] getParameters() {
		return (T[]) new Matrix[] { weights };
	}

	@Override
	public synchronized T getWeights() {
		return weights;
	}

	@Override
	public void setParameters(T[] parameters) {
		weights = parameters[0];
	}

	@Override
	public void setup(int previousNeurons) {
		this.previousNeurons = previousNeurons;

		weights = Matrix.randomMatrix(type, neurons, previousNeurons, new Random(), -1, 1);

	}

	@Override
	public synchronized void setWeights(T newWeights) {
		weights = newWeights;
	}

	@Override
	public Layer<CPUMatrix> contvertToCPU() {
		FeedFowardLayer<CPUMatrix> newLayer = new FeedFowardLayer<CPUMatrix>(neurons, MatrixType.CPU);
		newLayer.previousNeurons = previousNeurons;
		newLayer.weights = weights.toMatrix(MatrixType.CPU);

		return newLayer;
	}

}
