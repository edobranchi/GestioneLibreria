import me.tongfei.progressbar.ProgressBar;
public class avanzamento implements avanzamentoObserver{
    ProgressBar pb = new ProgressBar("Creazione DB",100);
    @Override
    public void update(int progressoquery) {
            this.pb.stepTo(progressoquery);
            this.pb.setExtraMessage("Attendere Prego");
    }
}
