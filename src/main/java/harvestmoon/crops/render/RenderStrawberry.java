package harvestmoon.crops.render;


public class RenderStrawberry extends RenderCrop {
    @Override
    public void renderCrop(int stage) {        
        switch(stage) {
            case 1: renderStage1();
            case 2: renderStage2();
            case 3: renderStage3();
            case 4: renderStage4();
            case 5: renderStage5();
            case 6: renderStage6();
            case 7: renderStage7();
            case 8: renderStage8();
            case 9: renderStage9();
        }
    }
    
    //Stage 1 Seeds
    public void renderStage1() {
        setTexture(getColor(Colors.STRAWBERRY));
        renderBlock(0.1D, 0D, 0.1D, 0.2D, 0.025D, 0.2D);
        renderBlock(0.4D, 0D, 0.25D, 0.5D, 0.025D, 0.35D);
        renderBlock(0.75D, 0D, 0.15D, 0.85D, 0.025D, 0.25D);
        renderBlock(0.7D, 0D, 0.75D, 0.8D, 0.025D, 0.85D);
        renderBlock(0.3D, 0D, 0.8D, 0.4D, 0.025D, 0.9D);
        renderBlock(0.5D, 0D, 0.55D, 0.6D, 0.025D, 0.65D);
        renderBlock(0.05D, 0D, 0.5D, 0.15D, 0.025D, 0.6D);
    }
    
    //Stage 2 Beginning to Sprout
    public void renderStage2() {
        
    }
    
    //Sprouted
    public void renderStage3() {
        
    }
    
    //Short Bush
    public void renderStage4() {
        
    }
    
    //Medium Bush
    public void renderStage5() {
        
    }
    
    //Fully Grown, No Fruit
    public void renderStage6() {
        
    }
    
    //Stage 7 Fully Grown, Green Fruit
    public void renderStage7() {
        
    }
    
    //Stage 8 Fully Grown Yellow Fruit
    public void renderStage8() {
        
    }
    
    //Stage 9 Fully Red and Ripe
    public void renderStage9() {
        
    }
}
