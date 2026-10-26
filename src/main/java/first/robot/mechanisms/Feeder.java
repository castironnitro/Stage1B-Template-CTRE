package first.robot.mechanisms;

import org.wpilib.command3.Command;
import org.wpilib.command3.Mechanism;
import org.wpilib.framework.RobotBase;
import org.wpilib.hardware.bus.CANPort;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.hardware.TalonFX;

import first.robot.simulation.SingleFlywheelSim;

public class Feeder implements Mechanism {
    private final TalonFX motor = new TalonFX (5, new CANBus(CANPort.CAN_S0));

    public Feeder() { // feeder constructor (no really :shocked:)
        setDefaultCommand(idle());
    }

    public Command feed() {
        return run(coroutine -> {
            while (true) {
                motor.setThrottle(0.75);
                coroutine.yield();
            }
        })
        .named("Feed");
    }

    public Command intake() {
        return run(coroutine -> {
            while (true) {
                motor.setThrottle(-1);
                coroutine.yield();
            }
        })
        .named("Intake");
    }

    public Command outtake() {
        return run(coroutine -> {
            while (true) {
                motor.setThrottle(1);
                coroutine.yield();
            }
        })
        .named("Outtake");
    }

    public Command idle() { // default command
        return run(coroutine -> {
            while (true) {
                motor.setThrottle(0);
                coroutine.yield();
            }
        })
        .named("Idle");
    }

    private final SingleFlywheelSim sim = SingleFlywheelSim.forFeeder(motor);

    public void periodic() { // update the simulation
    if (RobotBase.isSimulation()) {
        sim.periodic();
        }
    }

}
