package youga.vincihttp.internal;

/**
 * @author YougaKingWu
 * @descibe ...
 * @date 2017/10/10 0010-9:53
 */

public abstract class NamedRunnable implements Runnable{
    protected final String name;

    public NamedRunnable(String format, Object... args) {
        this.name = Util.format(format, args);
    }

    @Override public final void run() {
        String oldName = Thread.currentThread().getName();
        Thread.currentThread().setName(name);
        try {
            execute();
        } finally {
            Thread.currentThread().setName(oldName);
        }
    }

    protected abstract void execute();
}
