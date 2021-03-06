package logisticspipes.gui.modules;

import logisticspipes.modules.ModuleModBasedItemSink;
import logisticspipes.network.GuiIDs;
import logisticspipes.utils.ItemIdentifier;
import logisticspipes.utils.gui.BasicGuiHelper;
import logisticspipes.utils.gui.DummyContainer;
import logisticspipes.utils.gui.SmallGuiButton;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.IInventory;
import buildcraft.core.utils.SimpleInventory;
import buildcraft.transport.Pipe;

public class GuiModBasedItemSink extends GuiWithPreviousGuiContainer {
	
	private final ModuleModBasedItemSink _itemSink;
	private final SimpleInventory tmpInv;
	private String name = "";

	private int mouseX = 0;
	private int mouseY = 0;
	
	public GuiModBasedItemSink(IInventory playerInventory, Pipe pipe, ModuleModBasedItemSink itemSink, GuiScreen previousGui, int slot) {
		super(null, pipe, previousGui);
		
		_itemSink = itemSink;
		
		tmpInv = new SimpleInventory(1, "Analyse Slot", 1);
		
		DummyContainer dummy = new DummyContainer(playerInventory, tmpInv);
		dummy.addDummySlot(0, 7, 8);
		
		dummy.addNormalSlotsForPlayerInventory(7, 126);
	    
	    this.inventorySlots = dummy;
		xSize = 175;
		ySize = 208;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		super.initGui();
		this.controlList.clear();
		this.controlList.add(new SmallGuiButton(0, guiLeft + 38, guiTop + 18, 50, 10, "Add"));
		this.controlList.add(new SmallGuiButton(1, guiLeft + 107, guiTop + 18, 50, 10, "Remove"));
		((GuiButton)controlList.get(0)).enabled = false;
		((GuiButton)controlList.get(1)).enabled = false;
	}

	@Override
	public int getGuiID() {
		return GuiIDs.GUI_Module_ModBased_ItemSink_ID;
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		if(par1GuiButton.id == 0) {
			if(!_itemSink.modList.contains(ItemIdentifier.get(tmpInv.getStackInSlot(0)).getModName())) {
				_itemSink.modList.add(ItemIdentifier.get(tmpInv.getStackInSlot(0)).getModName());
				_itemSink.ModListChanged();
			}
		} else if(par1GuiButton.id == 1) {
			if(tmpInv.getStackInSlot(0) != null && _itemSink.modList.contains(ItemIdentifier.get(tmpInv.getStackInSlot(0)).getModName())) {
				_itemSink.modList.remove(ItemIdentifier.get(tmpInv.getStackInSlot(0)).getModName());
				_itemSink.ModListChanged();
			} else if(!name.equals("") && _itemSink.modList.contains(name)) {
				_itemSink.modList.remove(name);
				_itemSink.ModListChanged();
			}
		} else {
			super.actionPerformed(par1GuiButton);			
		}
	}

	@Override
	protected void mouseClicked(int i, int j, int k) {
		int x = i - guiLeft;
		int y = j - guiTop;
		if(0 < x && x < 175 && 0 < y && y < 208) {
			mouseX = x;
			mouseY = y;
		}
		super.mouseClicked(i, j, k);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		BasicGuiHelper.drawGuiBackGround(mc, guiLeft, guiTop, right, bottom, zLevel, true);
		BasicGuiHelper.drawPlayerInventoryBackground(mc, guiLeft + 7, guiTop + 126);
		BasicGuiHelper.drawSlotBackground(mc, guiLeft + 6, guiTop + 7);
		BasicGuiHelper.drawRect(guiLeft + 26, guiTop + 5, guiLeft + 169, guiTop + 17, 0xff808080);
		if(tmpInv.getStackInSlot(0) != null) {
			name = "";
			mc.fontRenderer.drawString(ItemIdentifier.get(tmpInv.getStackInSlot(0)).getModName(), guiLeft + 28, guiTop + 7, 0x404040);
			if(_itemSink.modList.contains(ItemIdentifier.get(tmpInv.getStackInSlot(0)).getModName())) {
				((GuiButton)controlList.get(0)).enabled = false;
				((GuiButton)controlList.get(1)).enabled = true;
			} else if (_itemSink.modList.size() < 9) {
				((GuiButton)controlList.get(0)).enabled = true;
				((GuiButton)controlList.get(1)).enabled = false;
			} else {
				((GuiButton)controlList.get(0)).enabled = false;
				((GuiButton)controlList.get(1)).enabled = false;
			}
		} else if(name.equals("")) {
			((GuiButton)controlList.get(0)).enabled = false;
			((GuiButton)controlList.get(1)).enabled = false;
		} else {
			if(_itemSink.modList.contains(name)) {
				mc.fontRenderer.drawString(name, guiLeft + 28, guiTop + 7, 0x404040);
				((GuiButton)controlList.get(0)).enabled = false;
				((GuiButton)controlList.get(1)).enabled = true;
			} else {
				name = "";
				((GuiButton)controlList.get(0)).enabled = false;
				((GuiButton)controlList.get(1)).enabled = false;
			}
		}
		BasicGuiHelper.drawRect(guiLeft + 5, guiTop + 30, guiLeft + 169, guiTop + 122, 0xff808080);
		for(int i=0; i < _itemSink.modList.size() && i < 9;i++) {
			mc.fontRenderer.drawString(_itemSink.modList.get(i), guiLeft + 7, guiTop + 32 + (10 * i), 0x404040);
			if(7 < mouseX && mouseX < 168 && 32 + (10 * i) < mouseY && mouseY < 32 + (10 * (i + 1))) {
				name = _itemSink.modList.get(i);
				mouseX = 0;
				mouseY = 0;
				tmpInv.setInventorySlotContents(0, null);
			}
		}
	}
}
